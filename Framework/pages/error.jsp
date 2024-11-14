<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erreur - Display Personne</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            color: #333;
            margin: 0;
            padding: 0;
        }

        h1 {
            background-color: #7820d1; /* Couleur de fond violet */
            color: white;
            padding: 15px;
            text-align: center;
            margin-bottom: 20px;
        }

        p {
            font-size: 16px;
            text-align: center;
            padding: 10px;
            background-color: white;
            border: 1px solid #ddd;
            margin: 0 15px;
            border-radius: 5px;
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>
<body>

    <div class="container">
        <h1>Erreur</h1>
        <p><%= request.getAttribute("error") %></p>
    </div>

</body>
</html>
