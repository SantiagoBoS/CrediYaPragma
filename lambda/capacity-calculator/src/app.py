import json
import os
import logging
import boto3
from capacity_calculator import CapacityCalculator

logger = logging.getLogger()
logger.setLevel(logging.INFO)

def lambda_handler(event, context):
    """
    Lambda function para calcular capacidad de endeudamiento
    """
    logger.info("Lambda capacity-calculator iniciada")
    logger.info(f"Event received: {json.dumps(event)}")

    calculator = CapacityCalculator()
    sqs_client = boto3.client('sqs', region_name='us-east-1')

    # URL de la cola de notificaciones (debe estar en variables de entorno)
    notification_queue_url = os.environ.get('NOTIFICATION_QUEUE_URL')

    for record in event.get('Records', []):
        try:
            message_body = json.loads(record['body'])
            logger.info(f"Processing capacity calculation request: {message_body}")

            # Validar que sea el tipo de mensaje correcto
            if message_body.get('type') != 'CAPACITY_CALCULATION':
                logger.warning(f"Tipo de mensaje no manejado: {message_body.get('type')}")
                continue

            # Procesar cálculo de capacidad
            result = calculator.calculate_capacity(message_body)
            logger.info(f"Capacity calculation completed: {result}")

            #Encolar notificación para que notification-handler envíe el email
            if result.get('decision') in ['APPROVED', 'REJECTED', 'MANUAL_REVIEW']:
                notify_decision_via_sqs(sqs_client, notification_queue_url, result)
                logger.info(f"Notificación encolada para loan: {result['loan_request_id']}")

            # TODO: En producción, aquí actualizarías la base de datos
            # calculator.update_loan_request(result)

        except Exception as e:
            logger.error(f"Error processing message: {str(e)}")
            # En producción, aquí enviarías a una cola de dead-letter

    return {
        'statusCode': 200,
        'body': json.dumps('Capacity calculations processed successfully')
    }

def notify_decision_via_sqs(sqs_client, queue_url, result):
    """
    Encola mensaje a la cola de notificaciones para que
    notification-handler envíe el email
    """
    # Validar que loan_type esté presente
    loan_type = result.get('loan_type')
    if not loan_type:
        logger.error("loan_type no disponible en el resultado, no se puede enviar notificación")
        return

    message = {
        "type": "LOAN_STATUS_UPDATE",
        "loanRequestId": result['loan_request_id'],
        "newStatus": result['decision'],  # "APPROVED", "REJECTED" o "MANUAL_REVIEW"
        "userEmail": result['user_email'],
        "loanType": loan_type,  # Tipo de solicitud
        "source": "AUTOMATIC_CALCULATION",  # Para identificar que viene de cálculo automático
        "paymentPlan": result.get('payment_plan')  # Solo si es APPROVED o MANUAL_REVIEW
    }

    sqs_client.send_message(
        QueueUrl=queue_url,
        MessageBody=json.dumps(message)
    )