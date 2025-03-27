import requests
import random
import string
import time


API_URL = "http://localhost:8081/api/person/register"


def random_name():
    first_names = ["John", "Jane", "Alex", "Chris", "Sam", "Taylor", "Jordan", "Morgan", "Casey", "Charlie"]
    last_names = ["Smith", "Doe", "Brown", "Johnson", "Davis", "Wilson", "Anderson", "Martinez", "Harris", "Clark"]
    return f"{random.choice(first_names)} {random.choice(last_names)}"


def random_email(name):
    domains = ["gmail.com", "yahoo.com", "outlook.com", "example.com"]
    username = name.lower().replace(" ", ".") + str(random.randint(100, 999))
    return f"{username}@{random.choice(domains)}"


def random_registration_number():
    return "".join(random.choices(string.ascii_uppercase + string.digits, k=10))


def random_user_type():
    return random.choice(["DELIVERY_MAN", "CUSTOMER"])


def create_person():
    name = random_name()
    email = random_email(name)
    reg_number = random_registration_number()
    user_type = random_user_type()

    person_data = {
        "name": name,
        "email": email,
        "registration_number": reg_number,
        "type": user_type
    }

    try:
        response = requests.post(API_URL, json=person_data)
        if response.status_code == 200 or response.status_code == 201:
            print(f"Created: {name} ({user_type}) - {email}")
        else:
            print(f"Failed to create {name}: {response.text}")
    except requests.exceptions.RequestException as e:
        print(f"Error connecting to API: {e}")

# Call the function for 20 different persons
if __name__ == "__main__":
    for _ in range(20):
        create_person()
        time.sleep(1)  