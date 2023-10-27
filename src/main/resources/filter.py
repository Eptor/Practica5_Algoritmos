import json

def filter_cards(json_data):
    """
    Filtra y elimina las cartas que no tengan las claves 'level', 'atk' o 'def'.
    """
    return [card for card in json_data if all(key in card for key in ['level', 'atk', 'def'])]

def main():
    filepath = "archivo.json"
    
    # Leer el archivo JSON
    with open(filepath, 'r', encoding='utf-8') as file:
        data = json.load(file)["data"]

    # Filtrar las cartas
    filtered_data = filter_cards(data)

    # Guardar los datos filtrados
    with open(filepath, 'w', encoding='utf-8') as file:
        json.dump({"data": filtered_data}, file, indent=4, ensure_ascii=False)

    print("Filtrado completo.")

if __name__ == "__main__":
    main()
