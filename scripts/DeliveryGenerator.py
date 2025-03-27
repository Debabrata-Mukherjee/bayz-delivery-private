import requests
import random
import datetime

# API Endpoints
BASE_URL = "http://localhost:8081/api"  
GET_PERSONS_URL = f"{BASE_URL}/person"
POST_DELIVERY_URL = f"{BASE_URL}/delivery"

# Fetch all persons
response = requests.get(GET_PERSONS_URL)
if response.status_code != 200:
    print("Error fetching persons:", response.text)
    exit()

persons = response.json()


delivery_men = [p for p in persons if p.get("type") == "DELIVERY_MAN"]
customers = [p for p in persons if p.get("type") == "CUSTOMER"]

# Ensure we have enough data
if len(delivery_men) == 0 or len(customers) == 0:
    print("Not enough delivery men or customers!")
    exit()

# Generate 40 deliveries
deliveries = []
for _ in range(40):
    delivery_man = random.choice(delivery_men)
    customer = random.choice(customers)


    start_time = datetime.datetime.now() - datetime.timedelta(days=random.randint(0, 1))
    

    end_time = start_time + datetime.timedelta(hours=random.randint(30, 100))

    delivery = {
        "deliveryMan": {"id": delivery_man["id"]},
        "customer": {"id": customer["id"]},
        "startTime": start_time.isoformat(),
        "endTime": end_time.isoformat(),
        "commission": random.randint(100, 1000),
        "price": random.randint(1000,10000),
        "distance": random.randint(0,50)
    }

    deliveries.append(delivery)

# Send deliveries to the API
for i, delivery in enumerate(deliveries):
    response = requests.post(POST_DELIVERY_URL, json=delivery)
    if response.status_code == 201:
        print(f"Delivery {i+1} successfully created!")
    else:
        print(f"Failed to create delivery {i+1}: {response.text}")
