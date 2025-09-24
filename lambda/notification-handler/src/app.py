import json
import os
import boto3
import logging

# Configurar logging
logger = logging.getLogger()
logger.setLevel(logging.INFO)

def lambda_handler(event, context):
    """
    Lambda function para procesar notificaciones de SQS y enviar emails via SNS
    - Recibe notificaciones MANUALES (de asesores)
    - Recibe notificaciones AUTOMÁTICAS (de capacity-calculator)
    """
    logger.info("Lambda notification-handler iniciada")
    logger.info(f"Event received: {json.dumps(event)}")

    # Cliente SNS para enviar emails
    sns_client = boto3.client('sns', region_name='us-east-1')

    # Procesar cada mensaje de SQS
    for record in event.get('Records', []):
        try:
            message_body = json.loads(record['body'])
            logger.info(f"Processing message: {message_body}")

            # Extraer datos del mensaje
            loan_request_id = message_body.get('loanRequestId', 'N/A')
            new_status = message_body.get('newStatus', '').upper()
            message_type = message_body.get('type')
            source = message_body.get('source', 'MANUAL')  # MANUAL o AUTOMATIC_CALCULATION
            user_email = message_body.get('userEmail', '')
            loan_type = message_body.get('loanType')  # Tipo de solicitud
            payment_plan = message_body.get('paymentPlan')

            # Validar tipo de mensaje
            if message_type != 'LOAN_STATUS_UPDATE':
                logger.warning(f"Tipo de mensaje no manejado: {message_type}")
                continue

            # Validar email del usuario
            if not user_email:
                logger.error("Email del usuario no proporcionado")
                continue

            # Validar tipo de préstamo
            if not loan_type:
                logger.error("Tipo de préstamo no proporcionado")
                continue

            logger.info(f"Notificando a: {user_email}, Estado: {new_status}, Fuente: {source}, Tipo: {loan_type}")

            # Construir email según la fuente (manual o automática)
            if source == 'AUTOMATIC_CALCULATION':
                subject, message = build_automatic_email(loan_request_id, new_status, payment_plan, loan_type)
            else:
                subject, message = build_manual_email(loan_request_id, new_status, loan_type)

            # Enviar email via SNS
            response = sns_client.publish(
                TopicArn=os.environ['SNS_TOPIC_ARN'],
                Message=message,
                Subject=subject,
                MessageAttributes={
                    'email': {
                        'DataType': 'String',
                        'StringValue': user_email
                    }
                }
            )

            logger.info(f"Email enviado. MessageId: {response['MessageId']}, Fuente: {source}")

        except Exception as e:
            logger.error(f"Error procesando mensaje: {str(e)}")

    return {
        'statusCode': 200,
        'body': json.dumps('Notificaciones procesadas correctamente')
    }

def build_manual_email(loan_request_id, new_status, loan_type):
    """
    Construye email para notificaciones MANUALES (de asesores)
    """
    loan_type_text = map_loan_type_to_spanish(loan_type)

    if new_status == 'APPROVED':
        subject = f'¡Préstamo {loan_type_text} Aprobado! - CrediYa'
        message = f"""
Estimado cliente,

¡Excelentes noticias! Su solicitud de préstamo {loan_type_text} #{loan_request_id} ha sido APROBADA por nuestro equipo de asesores.

Nuestro equipo se comunicará con usted en las próximas 24 horas para coordinar el desembolso.

- ID de Solicitud: {loan_request_id}
- Tipo de Préstamo: {loan_type_text}
- Estado: APROBADA

Gracias por confiar en CrediYa.

Atentamente,
Equipo CrediYa
        """
    elif new_status == 'REJECTED':
        subject = f'Resultado de su Préstamo {loan_type_text} - CrediYa'
        message = f"""
Estimado cliente,

Lamentamos informarle que su solicitud de préstamo {loan_type_text} #{loan_request_id} no ha sido aprobada luego de la revisión manual de nuestro equipo.

- ID de Solicitud: {loan_request_id}
- Tipo de Préstamo: {loan_type_text}
- Estado: RECHAZADA

Puede contactar a nuestro centro de atención al cliente para más información.

Atentamente,
Equipo CrediYa
        """
    else:
        raise ValueError(f"❌ Estado no manejado para notificación manual: {new_status}")

    return subject, message.strip()

def build_automatic_email(loan_request_id, new_status, payment_plan, loan_type):
    """
    Construye email para notificaciones AUTOMÁTICAS (de capacity-calculator)
    """
    loan_type_text = map_loan_type_to_spanish(loan_type)

    if new_status == 'APPROVED':
        subject = f'¡Préstamo {loan_type_text} Aprobado Automáticamente! - CrediYa'
        message = build_approval_message(loan_request_id, payment_plan, loan_type_text, is_automatic=True)
    elif new_status == 'MANUAL_REVIEW':
        subject = f'Préstamo {loan_type_text} en Revisión Manual - CrediYa'
        message = build_manual_review_message(loan_request_id, payment_plan, loan_type_text)
    elif new_status == 'REJECTED':
        subject = f'Resultado de Evaluación Automática - Préstamo {loan_type_text} - CrediYa'
        message = build_rejection_message(loan_request_id, loan_type_text, is_automatic=True)
    else:
        raise ValueError(f"❌ Estado no manejado para notificación automática: {new_status}")

    return subject, message

def build_approval_message(loan_request_id, payment_plan, loan_type_text, is_automatic):
    """Construye mensaje de aprobación"""
    base_message = f"Estimado cliente,\n\n"

    if is_automatic:
        base_message += f"¡Felicidades! Su solicitud de préstamo {loan_type_text} #{loan_request_id} ha sido **APROBADA AUTOMÁTICAMENTE** luego de la evaluación de capacidad de endeudamiento.\n\n"
    else:
        base_message += f"¡Excelentes noticias! Su solicitud de préstamo {loan_type_text} #{loan_request_id} ha sido APROBADA.\n\n"

    if payment_plan:
        plan_table = format_payment_plan_table(payment_plan)
        base_message += f"**Plan de pago aprobado:**\n\n{plan_table}\n\n"
        base_message += "El desembolso se realizará en un plazo máximo de 48 horas.\n\n"
    else:
        base_message += "Nuestro equipo se comunicará con usted en las próximas 24 horas para coordinar el desembolso.\n\n"

    base_message += "Gracias por confiar en CrediYa.\n\nAtentamente,\nEquipo CrediYa"
    return base_message

def build_manual_review_message(loan_request_id, payment_plan, loan_type_text):
    """Construye mensaje para revisión manual"""
    message = f"Estimado cliente,\n\n"
    message += f"Su solicitud de préstamo {loan_type_text} #{loan_request_id} requiere **REVISIÓN MANUAL** por exceder 5 salarios mínimos.\n\n"

    if payment_plan:
        plan_table = format_payment_plan_table(payment_plan)
        message += f"**Plan de pago tentativo (sujeto a revisión):**\n\n{plan_table}\n\n"

    message += "Un asesor se contactará con usted en las próximas 48 horas para finalizar la evaluación.\n\n"
    message += "Gracias por confiar en CrediYa.\n\nAtentamente,\nEquipo CrediYa"
    return message

def build_rejection_message(loan_request_id, loan_type_text, is_automatic):
    """Construye mensaje de rechazo"""
    if is_automatic:
        return f"""
Estimado cliente,

Lamentamos informarle que su solicitud de préstamo {loan_type_text} #{loan_request_id} no ha sido aprobada luego de la **evaluación automática** de capacidad de endeudamiento.

- ID de Solicitud: {loan_request_id}
- Tipo de Préstamo: {loan_type_text}
- Estado: RECHAZADA
- Motivo: Capacidad de endeudamiento insuficiente

Puede contactar a nuestro centro de atención al cliente para más información sobre cómo mejorar su perfil crediticio.

Atentamente,
Equipo CrediYa
        """.strip()
    else:
        return f"""
Estimado cliente,

Lamentamos informarle que su solicitud de préstamo {loan_type_text} #{loan_request_id} no ha sido aprobada.

- ID de Solicitud: {loan_request_id}
- Tipo de Préstamo: {loan_type_text}
- Estado: RECHAZADA

Puede contactar a nuestro centro de atención al cliente para más información.

Atentamente,
Equipo CrediYa
        """.strip()

def format_payment_plan_table(payment_plan):
    """Formatea el plan de pago como tabla legible"""
    if not payment_plan:
        return "No se pudo generar el plan de pago."

    table_header = "Mes | Cuota Mensual | Abono a Capital | Intereses | Saldo Restante\n"
    table_header += "-" * 70 + "\n"

    table_rows = []
    for payment in payment_plan:
        row = f"{payment['month']:3d} | ${payment['payment']:12.2f} | ${payment['principal']:12.2f} | ${payment['interest']:10.2f} | ${payment['remaining_balance']:12.2f}"
        table_rows.append(row)

    return table_header + "\n".join(table_rows)

def map_loan_type_to_spanish(loan_type):
    """Convierte el tipo de préstamo a español"""
    mapping = {
        'PERSONAL': 'Personal',
        'CAR': 'Vehicular',
        'MORTGAGE': 'Hipotecario'
    }
    return mapping.get(loan_type, loan_type)