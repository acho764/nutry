/**
 * Dish Ingredients Mapping for Nutry App
 * This file shows the ingredient composition for each Bulgarian dish
 * before adding them to the database
 */

data class DishIngredientMapping(
    val dishName: String,
    val emoji: String,
    val ingredients: List<String>
)

val bulgariанDishIngredients = listOf(
    DishIngredientMapping(
        dishName = "Пържени Картофи",
        emoji = "🍟",
        ingredients = listOf("Картофи", "Олио за пържене", "Сол")
    ),
    
    DishIngredientMapping(
        dishName = "Чушки Бюрек",
        emoji = "🥙",
        ingredients = listOf("Чушка (червена, зелена, люта)", "Сирене", "Яйца", "Брашно", "Олио за пържене")
    ),
    
    DishIngredientMapping(
        dishName = "Миш Маш",
        emoji = "🍳",
        ingredients = listOf("Яйца", "Сирене", "Домат", "Олио за пържене", "Сол", "Пипер")
    ),
    
    DishIngredientMapping(
        dishName = "Таратор",
        emoji = "🥒",
        ingredients = listOf("Краставица", "Кисело мляко", "Орехи", "Чесън", "Копър", "Олио", "Сол")
    ),
    
    DishIngredientMapping(
        dishName = "Тиквички",
        emoji = "🥒",
        ingredients = listOf("Тиквички", "Яйца", "Сирене", "Копър", "Олио за пържене", "Сол")
    ),
    
    DishIngredientMapping(
        dishName = "Грахова манджа",
        emoji = "🍲",
        ingredients = listOf("Грах", "Моркови", "Лук (жълт, червен, зелен)", "Домат", "Олио", "Сол", "Пипер")
    ),
    
    DishIngredientMapping(
        dishName = "Зелен боб",
        emoji = "🥗",
        ingredients = listOf("Зелен фасул", "Лук (жълт, червен, зелен)", "Моркови", "Домат", "Олио", "Сол", "Пипер")
    ),
    
    DishIngredientMapping(
        dishName = "Спанак",
        emoji = "🥬",
        ingredients = listOf("Спанак", "Яйца", "Сирене", "Лук (жълт, червен, зелен)", "Олио", "Сол")
    ),
    
    DishIngredientMapping(
        dishName = "ПИЦА",
        emoji = "🍕",
        ingredients = listOf("Брашно", "Кашкавал", "Домат", "Олио", "Сол", "Мая")
    ),
    
    DishIngredientMapping(
        dishName = "Кус-кус",
        emoji = "🍚",
        ingredients = listOf("Кус-кус", "Зеленчуци (микс)", "Олио", "Сол", "Пипер")
    ),
    
    DishIngredientMapping(
        dishName = "Макарони",
        emoji = "🍝",
        ingredients = listOf("Макарони", "Кашкавал", "Масло", "Сол")
    ),
    
    DishIngredientMapping(
        dishName = "Спагети",
        emoji = "🍝",
        ingredients = listOf("Спагети", "Домат", "Кашкавал", "Олио", "Чесън", "Сол")
    ),
    
    DishIngredientMapping(
        dishName = "Овес с мляко",
        emoji = "🥣",
        ingredients = listOf("Овес", "Мляко", "Захар", "Мед")
    ),
    
    DishIngredientMapping(
        dishName = "Мюсли",
        emoji = "🥣",
        ingredients = listOf("Мюсли", "Мляко", "Плодове (микс)", "Мед")
    ),
    
    DishIngredientMapping(
        dishName = "Палачинки",
        emoji = "🥞",
        ingredients = listOf("Брашно", "Мляко", "Яйца", "Захар", "Масло", "Сол")
    ),
    
    DishIngredientMapping(
        dishName = "Боб",
        emoji = "🫘",
        ingredients = listOf("Боб", "Лук (жълт, червен, зелен)", "Моркови", "Домат", "Олио", "Сол", "Пипер")
    ),
    
    DishIngredientMapping(
        dishName = "Леща",
        emoji = "🫘",
        ingredients = listOf("Леща", "Лук (жълт, червен, зелен)", "Моркови", "Олио", "Сол", "Пипер")
    ),
    
    DishIngredientMapping(
        dishName = "Пържени филийки",
        emoji = "🍞",
        ingredients = listOf("Хляб", "Мляко", "Яйца", "Олио за пържене", "Сол")
    ),
    
    DishIngredientMapping(
        dishName = "Баница",
        emoji = "🥐",
        ingredients = listOf("Кори за баница", "Сирене", "Яйца", "Мляко", "Олио", "Сол")
    ),
    
    DishIngredientMapping(
        dishName = "Бутерки",
        emoji = "🥪",
        ingredients = listOf("Хляб", "Кашкавал", "Шунка", "Домат", "Краставица", "Масло")
    ),
    
    DishIngredientMapping(
        dishName = "Гюзлеме",
        emoji = "🥙",
        ingredients = listOf("Брашно", "Сирене", "Спанак", "Олио", "Сол", "Вода")
    ),
    
    DishIngredientMapping(
        dishName = "Запеканка с картофи и зеленчуци",
        emoji = "🍲",
        ingredients = listOf("Картофи", "Тиквички", "Чушка (червена, зелена, люта)", "Кашкавал", "Яйца", "Мляко", "Олио", "Сол")
    ),
    
    DishIngredientMapping(
        dishName = "Гювече с кашкавал и сос барбекю",
        emoji = "🍲",
        ingredients = listOf("Картофи", "Кашкавал", "Лук (жълт, червен, зелен)", "Чушка (червена, зелена, люта)", "Олио", "Сол", "Пипер")
    ),
    
    DishIngredientMapping(
        dishName = "Крем супа с кротони",
        emoji = "🍲",
        ingredients = listOf("Картофи", "Моркови", "Лук (жълт, червен, зелен)", "Сметана", "Хляб", "Масло", "Сол", "Пипер")
    ),
    
    DishIngredientMapping(
        dishName = "Зеленчуково къри с картофи и грах",
        emoji = "🍛",
        ingredients = listOf("Картофи", "Грах", "Лук (жълт, червен, зелен)", "Моркови", "Къри", "Кокосово мляко", "Олио", "Сол")
    ),
    
    DishIngredientMapping(
        dishName = "Печени зеленчуци с кашкавал",
        emoji = "🥗",
        ingredients = listOf("Тиквички", "Чушка (червена, зелена, люта)", "Патладжан", "Кашкавал", "Олио", "Сол", "Пипер")
    ),
    
    DishIngredientMapping(
        dishName = "Тиквеник",
        emoji = "🥧",
        ingredients = listOf("Тиква", "Брашно", "Яйца", "Захар", "Олио", "Сол", "Канела")
    ),
    
    DishIngredientMapping(
        dishName = "Сметана",
        emoji = "🥛",
        ingredients = listOf("Сметана")
    ),
    
    DishIngredientMapping(
        dishName = "Шоколад",
        emoji = "🍫",
        ingredients = listOf("Шоколад")
    ),
    
    DishIngredientMapping(
        dishName = "Какао",
        emoji = "☕",
        ingredients = listOf("Какао", "Мляко", "Захар")
    )
)

// Display function to show dish ingredients before database insertion
fun displayDishIngredients() {
    println("=".repeat(80))
    println("BULGARIAN DISHES AND THEIR INGREDIENTS")
    println("=".repeat(80))
    
    bulgariанDishIngredients.forEachIndexed { index, dish ->
        println("${index + 1}. ${dish.emoji} ${dish.dishName}")
        println("   Ingredients: ${dish.ingredients.joinToString(", ")}")
        println("   Total ingredients: ${dish.ingredients.size}")
        println()
    }
    
    println("=".repeat(80))
    println("SUMMARY:")
    println("Total dishes: ${bulgariанDishIngredients.size}")
    println("Average ingredients per dish: ${bulgariанDishIngredients.map { it.ingredients.size }.average().toInt()}")
    println("Most complex dish: ${bulgariанDishIngredients.maxByOrNull { it.ingredients.size }?.dishName} (${bulgariанDishIngredients.maxByOrNull { it.ingredients.size }?.ingredients?.size} ingredients)")
    println("Simplest dish: ${bulgariанDishIngredients.minByOrNull { it.ingredients.size }?.dishName} (${bulgariанDishIngredients.minByOrNull { it.ingredients.size }?.ingredients?.size} ingredients)")
    println("=".repeat(80))
}

// Function to get unique ingredients across all dishes
fun getUniqueIngredients(): Set<String> {
    return bulgariанDishIngredients.flatMap { it.ingredients }.toSet()
}

// Function to check for missing ingredients in database
fun checkMissingIngredients(databaseIngredients: List<String>): List<String> {
    val dishIngredients = getUniqueIngredients()
    return dishIngredients.filter { it !in databaseIngredients }
}