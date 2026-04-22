# 🍽️ Recipe Recommendation System (Semantic Web Homework 1)

## 📌 Description
This project is a Java Spring Boot web application that recommends recipes to users based on their cooking skill level and preferred cuisine type.

The application demonstrates the use of **XML technologies** including XML, XSD, XPath, and XSL as required in a Semantic Web assignment.

---

## 🛠️ Technologies Used
- Java (Spring Boot)
- XML (data storage)
- XML Schema (XSD) for validation
- XPath for querying data
- XSLT for XML transformation
- Thymeleaf (UI templates)

---

## ⚙️ Features

### 📂 Data Management
- Recipes and users are stored in XML files
- XML is validated using XSD before saving
- Recipes can be added via UI forms
- Users can be added via UI forms

### 🌐 Web Scraping
- Recipes can be scraped from:
  https://www.bbcgoodfood.com/recipes/collection/budget-autumn
- Only titles are scraped
- Cuisine types and difficulty levels are assigned randomly

### 🔍 Querying (XPath)
- Recommend recipes by cooking skill level
- Recommend recipes by skill + preferred cuisine
- Filter recipes by cuisine type
- View recipe details by ID

### 🎨 XSL Transformation
- Recipes are displayed using XSLT
- Highlighting:
  - 🟡 Yellow = matches user skill
  - 🟢 Green = does not match

### 👤 UI Improvements
- Users can be selected for recommendations
- Navigation bar for easy access
- Clean and simple interface

---

## ▶️ How to Run

1. Clone the repository: git clone <your-repo-link> 
2. Open the project in IntelliJ
3. Run the main class (RecommendRecipes.java)
4. Open in browser: http://localhost:8080

## 📁 Project structure

src/main/java/com/example/
├── controller/
├── model/
├── service/
└── util/

src/main/resources/
├── data/
│   ├── recipes.xml
│   ├── users.xml
│   ├── recipes.xsd
│   └── users.xsd
├── templates/
└── xsl/

## Authors: Dan Maria-Andrada, Grec Carina-Gabriela
