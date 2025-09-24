# Para empaquetar el contenedor de la capacidad de endeudamiento
FROM public.ecr.aws/lambda/python:3.9
LABEL authors="SBS"

COPY requirements.txt .
RUN pip install -r requirements.txt --target "${LAMBDA_TASK_ROOT}"

COPY src/* ${LAMBDA_TASK_ROOT}/

CMD ["app.lambda_handler"]