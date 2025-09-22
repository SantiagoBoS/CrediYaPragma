import json
import os
import boto3
import logging

logger = logging.getLogger()
logger.setLevel(logging.INFO)

def lambda_handler(event, context):
    """
    Lambda function para procesar notificaciones de SQS y enviar emails via SNS
    """
    logger.info(f"Event received: {json.dumps(event)}")

    sns_client = boto3.client('sns', region_name='us-east-1')

    for record in event.get('Records', []):
        try:
            message_body = json.loads(record['body'])
            logger.info(f"Processing message: {message_body}")

            loan_request_id = message_body.get('loanRequestId', 'N/A')
            new_status = message_body.get('newStatus', '').upper()
            message_type = message_body.get('type')

            if message_type != 'LOAN_STATUS_UPDATE':
                logger.warning(f"Tipo de mensaje no manejado: {message_type}")
                continue

            if new_status == 'APPROVED':
                subject = '¡Solicitud de Préstamo Aprobada! - CrediYa'
                message = (
                    f"Estimado cliente,\n\n"
                    f"¡Excelentes noticias! Su solicitud de préstamo #{loan_request_id} ha sido APROBADA.\n\n"
                    "Nuestro equipo se comunicará con usted en las próximas 24 horas para coordinar el desembolso.\n\n"
                    f"- ID de Solicitud: {loan_request_id}\n"
                    f"- Estado: APROBADA\n\n"
                    "Gracias por confiar en CrediYa.\n\n"
                    "Atentamente,\nEquipo CrediYa"
                )
            elif new_status == 'REJECTED':
                subject = 'Resultado de su Solicitud de Préstamo - CrediYa'
                message = (
                    f"Estimado cliente,\n\n"
                    f"Lamentamos informarle que su solicitud de préstamo #{loan_request_id} no ha sido aprobada.\n\n"
                    f"- ID de Solicitud: {loan_request_id}\n"
                    f"- Estado: RECHAZADA\n\n"
                    "Puede contactar a nuestro centro de atención al cliente para más información.\n\n"
                    "Atentamente,\nEquipo CrediYa"
                )
            else:
                logger.warning(f"Estado no manejado para notificación: {new_status}")
                continue

            response = sns_client.publish(
                TopicArn=os.environ['SNS_TOPIC_ARN'],
                Message=message,
                Subject=subject
            )

            logger.info(f"Email publicado en SNS. MessageId: {response['MessageId']}")

        except Exception as e:
            logger.error(f"Error procesando mensaje: {str(e)}")

    return {
        'statusCode': 200,
        'body': json.dumps('Notificaciones procesadas correctamente')
    }
