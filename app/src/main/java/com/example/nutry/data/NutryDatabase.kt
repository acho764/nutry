package com.example.nutry.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.nutry.data.dao.*
import com.example.nutry.data.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        Category::class,
        Ingredient::class,
        Dish::class,
        DishIngredient::class,
        TrackEntry::class,
        Settings::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NutryDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun dishDao(): DishDao
    abstract fun trackDao(): TrackDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: NutryDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): NutryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NutryDatabase::class.java,
                    "nutry_database"
                )
                    .addCallback(NutryDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class NutryDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database)
                    }
                }
            }
        }

        suspend fun populateDatabase(database: NutryDatabase) {
            val settingsDao = database.settingsDao()
            val categoryDao = database.categoryDao()
            val ingredientDao = database.ingredientDao()
            val dishDao = database.dishDao()

            // Insert default settings
            settingsDao.insertSettings(Settings())
            
            // Insert categories
            val vegetablesId = categoryDao.insertCategory(Category(name = "Зеленчуци", emoji = "🥬"))
            val fruitsId = categoryDao.insertCategory(Category(name = "Плодове", emoji = "🍎"))
            val grainsId = categoryDao.insertCategory(Category(name = "Зърнени и тестени продукти", emoji = "🍞"))
            val spicesId = categoryDao.insertCategory(Category(name = "Подправки и подправъчни продукти", emoji = "🧂"))
            val fatsId = categoryDao.insertCategory(Category(name = "Мазнини", emoji = "🧈"))
            val meatId = categoryDao.insertCategory(Category(name = "Месо и месни продукти", emoji = "🥩"))
            val fishId = categoryDao.insertCategory(Category(name = "Риба и морски дарове", emoji = "🐟"))
            val dairyId = categoryDao.insertCategory(Category(name = "Млечни продукти и яйца", emoji = "🥚"))
            val sweetsId = categoryDao.insertCategory(Category(name = "Захарни и сладкарски продукти", emoji = "🍬"))
            val nutsId = categoryDao.insertCategory(Category(name = "Ядки и семена", emoji = "🥜"))
            
            // Insert vegetables
            val vegetableNames = listOf(
                "Картофи", "Моркови", "Лук (жълт, червен, зелен)", "Чесън", "Домат", "Краставица",
                "Чушка (червена, зелена, люта)", "Зеле", "Карфиол", "Броколи", "Тиквички", "Патладжан",
                "Тиква", "Спанак", "Лапад", "Киселец", "Грах", "Зелен фасул", "Целина", "Ряпа",
                "Аспержи", "Рукола", "Айсберг", "Салата (зелена, маруля, романа)", "Царевица (и консервирана)"
            )
            vegetableNames.forEach { name ->
                ingredientDao.insertIngredient(Ingredient(name = name, categoryId = vegetablesId.toInt()))
            }
            
            // Insert fruits
            val fruitNames = listOf(
                "Ябълка", "Круша", "Банан", "Портокал", "Лимон", "Мандарина", "Киви", "Ягода",
                "Малина", "Къпина", "Боровинка", "Череша", "Вишна", "Праскова", "Кайсия", "Слива",
                "Грозде", "Смокиня", "Нар", "Авокадо", "Ананас", "Манго", "Пъпеш", "Диня", "Лайм"
            )
            fruitNames.forEach { name ->
                ingredientDao.insertIngredient(Ingredient(name = name, categoryId = fruitsId.toInt()))
            }
            
            // Insert grains
            val grainNames = listOf(
                "Пшенично брашно", "Царевично брашно", "Овесени ядки", "Елда", "Киноа", "Булгур",
                "Ориз (бял, кафяв, жасминов, басмати)", "Хляб (бял, ръжен, пълнозърнест)", "Макарони / паста",
                "Спагети", "Корнфлейкс", "Кускус"
            )
            grainNames.forEach { name ->
                ingredientDao.insertIngredient(Ingredient(name = name, categoryId = grainsId.toInt()))
            }
            
            // Insert spices
            val spiceNames = listOf(
                "Сол", "Черен пипер", "Червен пипер (сладък, лют)", "Кимион", "Канела", "Джинджифил",
                "Карамфил", "Кардамон", "Дафинов лист", "Индийско орехче", "Ванилия", "Риган", "Босилек",
                "Мащерка", "Чубрица", "Магданоз", "Копър", "Розмарин", "Горчица", "Лютеница", "Хрян",
                "Соев сос", "Балсамов оцет", "Ябълков оцет", "Винен оцет"
            )
            spiceNames.forEach { name ->
                ingredientDao.insertIngredient(Ingredient(name = name, categoryId = spicesId.toInt()))
            }
            
            // Insert fats
            val fatNames = listOf(
                "Олио", "Зехтин", "Масло", "Краве мас", "Кокосово масло", "Палмово масло", "Маргарин"
            )
            fatNames.forEach { name ->
                ingredientDao.insertIngredient(Ingredient(name = name, categoryId = fatsId.toInt()))
            }
            
            // Insert meat
            val meatNames = listOf(
                "Свинско месо", "Телешко месо", "Агнешко", "Пилешко месо", "Пуйка", "Кайма (смес, свинска, телешка)",
                "Наденица", "Салам", "Шунка", "Бекон", "Луканка", "Саздърма", "Червен дроб"
            )
            meatNames.forEach { name ->
                ingredientDao.insertIngredient(Ingredient(name = name, categoryId = meatId.toInt()))
            }
            
            // Insert fish
            val fishNames = listOf(
                "Скумрия", "Пъстърва", "Сьомга", "Риба тон", "Хек", "Калмари", "Скариди", "Миди", "Октопод", "Цаца", "Херинга"
            )
            fishNames.forEach { name ->
                ingredientDao.insertIngredient(Ingredient(name = name, categoryId = fishId.toInt()))
            }
            
            // Insert dairy
            val dairyNames = listOf(
                "Прясно мляко", "Кисело мляко", "Сирене (бяло саламурено, краве, овче, козе)", "Кашкавал",
                "Извара", "Сметана (готварска, сладкарска)", "Маскарпоне", "Рикота", "Яйца (кокоши, пъдпъдъчи)"
            )
            dairyNames.forEach { name ->
                ingredientDao.insertIngredient(Ingredient(name = name, categoryId = dairyId.toInt()))
            }
            
            // Insert sweets
            val sweetNames = listOf(
                "Захар (бяла, кафява, пудра)", "Мед", "Меласа", "Шоколад", "Какао", "Сладкарски бои",
                "Ванилова захар", "Бакпулвер", "Сода бикарбонат", "Желатин", "Вафли", "Бисквити",
                "Кондензирано мляко", "Сладко", "Мармалад"
            )
            sweetNames.forEach { name ->
                ingredientDao.insertIngredient(Ingredient(name = name, categoryId = sweetsId.toInt()))
            }
            
            // Insert nuts
            val nutNames = listOf(
                "Орехи", "Бадеми", "Лешници", "Фъстъци", "Кашу", "Слънчогледови семки", "Тиквени семки",
                "Чия", "Ленено семе", "Кедрови ядки", "Кокосови стърготини"
            )
            nutNames.forEach { name ->
                ingredientDao.insertIngredient(Ingredient(name = name, categoryId = nutsId.toInt()))
            }
            
            // Insert dishes
            val dishNames = listOf(
                "Пържени Картофи" to "🍟", "Чушки Бюрек" to "🥙", "Миш Маш" to "🍳", "Таратор" to "🥒",
                "Тиквички" to "🥒", "Грахова манджа" to "🍲", "Зелен боб" to "🥗", "Спанак" to "🥬",
                "ПИЦА" to "🍕", "Кус-кус" to "🍚", "Макарони" to "🍝", "Спагети" to "🍝",
                "Овес с мляко" to "🥣", "Мюсли" to "🥣", "Палачинки" to "🥞", "Боб" to "🫘",
                "Леща" to "🫘", "Пържени филийки" to "🍞", "Баница" to "🥐", "Бутерки" to "🥪",
                "Гюзлеме" to "🥙", "Кашкавал" to "🧀", "Сирене" to "🧀", "Маргарин" to "🧈",
                "Запеканка с картофи и зеленчуци" to "🍲", "Гювече с кашкавал и сос барбекю" to "🍲",
                "Крем супа с кротони" to "🍲", "Зеленчуково къри с картофи и грах" to "🍛",
                "Печени зеленчуци с кашкавал" to "🥗", "Тиквеник" to "🥧", "Сметана" to "🥛",
                "Шоколад" to "🍫", "Какао" to "☕"
            )
            dishNames.forEach { (name, emoji) ->
                dishDao.insertDish(Dish(name = name, emoji = emoji))
            }
        }
    }
}