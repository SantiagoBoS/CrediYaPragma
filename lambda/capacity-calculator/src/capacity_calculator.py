import math
import logging

logger = logging.getLogger()

class CapacityCalculator:
    def __init__(self):
        # TODO: Configurar conexión a base de datos si es necesaria
        pass

    def calculate_capacity(self, loan_data):
        """
        Calcula la capacidad de endeudamiento según las fórmulas especificadas
        """
        try:
            # Extraer datos del mensaje con conversión explícita
            user_id = loan_data.get('user_id')
            user_email = loan_data.get('userEmail')
            loan_request_id = loan_data.get('loanRequestId')
            loan_amount = float(loan_data.get('loanAmount', 0))
            loan_term = int(loan_data.get('loanTerm', 0))  # en meses
            interest_rate = float(loan_data.get('interestRate', 0)) / 100  # convertir % a decimal
            user_salary = float(loan_data.get('userSalary', 0))
            loan_type = loan_data.get('loanType', 'PERSONAL')  # ✅ Viene del mensaje original

            # Validaciones básicas
            if loan_amount <= 0 or loan_term <= 0 or user_salary <= 0:
                raise ValueError("Datos del préstamo inválidos")

            logger.info(f"Calculating capacity for user {user_id}, loan {loan_request_id}, type: {loan_type}")

            # 1. Capacidad máxima de endeudamiento (35% del salario)
            max_capacity = user_salary * 0.35

            # 2. Deuda mensual actual (consultar préstamos activos del usuario)
            current_debt = self.get_current_monthly_debt(user_id)

            # 3. Capacidad disponible
            available_capacity = max_capacity - current_debt

            # 4. Cuota del nuevo préstamo
            new_loan_payment = self.calculate_loan_payment(loan_amount, interest_rate, loan_term)

            # 5. Lógica de decisión
            decision = self.make_decision(new_loan_payment, available_capacity, loan_amount, user_salary)

            # 6. Generar plan de pago si es aprobado o revisión manual
            payment_plan = None
            if decision in ['APPROVED', 'MANUAL_REVIEW']:
                payment_plan = self.generate_payment_plan(loan_amount, interest_rate, loan_term)

            return {
                'user_id': user_id,
                'user_email': user_email,
                'loan_request_id': loan_request_id,
                'loan_amount': loan_amount,
                'loan_term': loan_term,
                'interest_rate': interest_rate,
                'user_salary': user_salary,
                'loan_type': loan_type,  # ✅ Incluido en el resultado
                'max_capacity': round(max_capacity, 2),
                'current_debt': round(current_debt, 2),
                'available_capacity': round(available_capacity, 2),
                'new_loan_payment': round(new_loan_payment, 2),
                'decision': decision,
                'payment_plan': payment_plan
            }

        except Exception as e:
            logger.error(f"Error in capacity calculation: {str(e)}")
            raise

    def get_current_monthly_debt(self, user_id):
        """
        Consulta los préstamos activos del usuario y calcula la deuda mensual total
        TODO: Implementar conexión a BD o llamada a microservicio SOLICITUDES
        """
        try:
            # Por ahora retornar 0 (simulación)
            # En producción, aquí harías una consulta a la base de datos
            logger.info(f"Fetching current debt for user {user_id}")
            return 0.0  # Simulación - en producción esto vendría de la BD

        except Exception as e:
            logger.error(f"❌ Error fetching current debt: {str(e)}")
            return 0.0  # Fallback seguro

    def calculate_loan_payment(self, amount, monthly_rate, term_months):
        """
        Calcula la cuota mensual usando la fórmula corregida:
        R = (amount * monthly_rate) / (1 - (1 + monthly_rate)^(-term_months))
        """
        if monthly_rate == 0:
            return amount / term_months

        # Validar que la tasa mensual sea válida
        if monthly_rate <= 0 or monthly_rate >= 1:  # No puede ser 0% o >= 100%
            raise ValueError("Tasa de interés mensual inválida")

        numerator = amount * monthly_rate
        denominator = 1 - (1 + monthly_rate) ** (-term_months)

        if denominator <= 0:
            raise ValueError("Denominador inválido en cálculo de cuota")

        return numerator / denominator

    def make_decision(self, new_payment, available_capacity, loan_amount, user_salary):
        """
        Toma la decisión basada en la capacidad disponible
        """
        salary_threshold = user_salary * 5  # 5 salarios mínimos

        if new_payment <= available_capacity:
            if loan_amount > salary_threshold:
                return 'MANUAL_REVIEW'  # Revisión manual si > 5 salarios
            else:
                return 'APPROVED'
        else:
            return 'REJECTED'

    def generate_payment_plan(self, amount, monthly_rate, term_months):
        """
        Genera el plan de pago detallado (amortización)
        """
        monthly_payment = self.calculate_loan_payment(amount, monthly_rate, term_months)
        balance = amount
        payment_plan = []

        for month in range(1, term_months + 1):
            interest_payment = balance * monthly_rate
            principal_payment = monthly_payment - interest_payment
            balance -= principal_payment

            # Asegurar que en el último pago el balance sea 0
            if month == term_months:
                principal_payment += balance  # Ajuste final
                balance = 0

            payment_plan.append({
                'month': month,
                'payment': round(monthly_payment, 2),
                'principal': round(principal_payment, 2),
                'interest': round(interest_payment, 2),
                'remaining_balance': round(max(balance, 0), 2)
            })

        return payment_plan