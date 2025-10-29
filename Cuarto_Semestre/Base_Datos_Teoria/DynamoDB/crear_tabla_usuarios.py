import boto3

# Crear cliente DynamoDB en la región correcta
dynamodb = boto3.client(
    'dynamodb',
    region_name='us-east-2',
    aws_access_key_id='AKIAYLNWPBA4ULK73DRX',
    aws_secret_access_key='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'
)

# Crear tabla
tabla = dynamodb.create_table(
    TableName='Usuarios',
    KeySchema=[
        {'AttributeName': 'PK', 'KeyType': 'HASH'},  # Partition key
        {'AttributeName': 'SK', 'KeyType': 'RANGE'}  # Sort key
    ],
    AttributeDefinitions=[
        {'AttributeName': 'PK', 'AttributeType': 'S'},
        {'AttributeName': 'SK', 'AttributeType': 'S'}
    ],
    BillingMode='PAY_PER_REQUEST'  # Modo bajo demanda (no requiere capacidad fija)
)

print("✅ Tabla 'Usuarios' creada correctamente:")
print(tabla)
