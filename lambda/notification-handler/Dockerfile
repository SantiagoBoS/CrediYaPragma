# Para empaquetar el contenedor
FROM public.ecr.aws/lambda/python:3.9

# Copiar requirements e instalar dependencias
COPY requirements.txt .
RUN pip install -r requirements.txt --target "${LAMBDA_TASK_ROOT}"

# Copiar código de la Lambda
COPY src/* ${LAMBDA_TASK_ROOT}/

# Establecer el handler
CMD ["app.lambda_handler"]