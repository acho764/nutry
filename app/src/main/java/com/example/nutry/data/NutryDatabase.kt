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
    version = 3,
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
                    .fallbackToDestructiveMigration()
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
            val vegetableItems = listOf(
                "Картофи" to "🥔", "Моркови" to "🥕", "Лук (жълт, червен, зелен)" to "🧅", "Чесън" to "🧄", 
                "Домат" to "🍅", "Краставица" to "🥒", "Чушка (червена, зелена, люта)" to "🌶️", "Зеле" to "🥬", 
                "Карфиол" to "🥬", "Броколи" to "🥦", "Тиквички" to "🥒", "Патладжан" to "🍆",
                "Тиква" to "🎃", "Спанак" to "🥬", "Лапад" to "🥬", "Киселец" to "🥬", 
                "Грах" to "🟢", "Зелен фасул" to "🫛", "Целина" to "🥬", "Ряпа" to "🥬",
                "Аспержи" to "🥬", "Рукола" to "🥬", "Айсберг" to "🥬", "Салата (зелена, маруля, романа)" to "🥬", 
                "Царевица (и консервирана)" to "🌽"
            )
            vegetableItems.forEach { (name, emoji) ->
                ingredientDao.insertIngredient(Ingredient(name = name, categoryId = vegetablesId.toInt(), emoji = emoji))
            }
            
            // Insert fruits
            val fruitItems = listOf(
                "Ябълка" to "🍎", "Круша" to "🍐", "Банан" to "🍌", "Портокал" to "🍊", 
                "Лимон" to "🍋", "Мандарина" to "🍊", "Киви" to "🥝", "Ягода" to "🍓",
                "Малина" to "🫐", "Къпина" to "🫐", "Боровинка" to "🫐", "Череша" to "🍒", 
                "Вишна" to "🍒", "Праскова" to "🍑", "Кайсия" to "🍑", "Слива" to "🟣",
                "Грозде" to "🍇", "Смокиня" to "🟤", "Нар" to "🔴", "Авокадо" to "🥑", 
                "Ананас" to "🍍", "Манго" to "🥭", "Пъпеш" to "🍈", "Диня" to "🍉", "Лайм" to "🍋"
            )
            fruitItems.forEach { (name, emoji) ->
                ingredientDao.insertIngredient(Ingredient(name = name, categoryId = fruitsId.toInt(), emoji = emoji))
            }
            
            // Insert grains
            val grainItems = listOf(
                "Пшенично брашно" to "🌾", "Царевично брашно" to "🌽", "Овесени ядки" to "🌾", 
                "Елда" to "🌾", "Киноа" to "🌾", "Булгур" to "🌾",
                "Ориз (бял, кафяв, жасминов, басмати)" to "🍚", "Хляб (бял, ръжен, пълнозърнест)" to "🍞", 
                "Макарони / паста" to "🍝", "Спагети" to "🍝", "Корнфлейкс" to "🥣", "Кускус" to "🍚"
            )
            grainItems.forEach { (name, emoji) ->
                ingredientDao.insertIngredient(Ingredient(name = name, categoryId = grainsId.toInt(), emoji = emoji))
            }
            
            // Insert spices
            val spiceItems = listOf(
                "Сол" to "🧂", "Черен пипер" to "🫚", "Червен пипер (сладък, лют)" to "🌶️", 
                "Кимион" to "🧂", "Канела" to "🟤", "Джинджифил" to "🫚",
                "Карамфил" to "🟤", "Кардамон" to "🟤", "Дафинов лист" to "🌿", 
                "Индийско орехче" to "🟤", "Ванилия" to "🟤", "Риган" to "🌿", "Босилек" to "🌿",
                "Мащерка" to "🌿", "Чубрица" to "🌿", "Магданоз" to "🌿", "Копър" to "🌿", 
                "Розмарин" to "🌿", "Горчица" to "🟡", "Лютеница" to "🔴", "Хрян" to "⚪",
                "Соев сос" to "🟤", "Балсамов оцет" to "🟤", "Ябълков оцет" to "🟡", "Винен оцет" to "🍷"
            )
            spiceItems.forEach { (name, emoji) ->
                ingredientDao.insertIngredient(Ingredient(name = name, categoryId = spicesId.toInt(), emoji = emoji))
            }
            
            // Insert fats
            val fatItems = listOf(
                "Олио" to "🫒", "Зехтин" to "🫒", "Масло" to "🧈", "Краве мас" to "🧈", 
                "Кокосово масло" to "🥥", "Палмово масло" to "🫒", "Маргарин" to "🧈"
            )
            fatItems.forEach { (name, emoji) ->
                ingredientDao.insertIngredient(Ingredient(name = name, categoryId = fatsId.toInt(), emoji = emoji))
            }
            
            // Insert meat
            val meatItems = listOf(
                "Свинско месо" to "🥩", "Телешко месо" to "🥩", "Агнешко" to "🥩", 
                "Пилешко месо" to "🍗", "Пуйка" to "🍗", "Кайма (смес, свинска, телешка)" to "🥩",
                "Наденица" to "🌭", "Салам" to "🥓", "Шунка" to "🥓", "Бекон" to "🥓", 
                "Луканка" to "🥓", "Саздърма" to "🌭", "Червен дроб" to "🥩"
            )
            meatItems.forEach { (name, emoji) ->
                ingredientDao.insertIngredient(Ingredient(name = name, categoryId = meatId.toInt(), emoji = emoji))
            }
            
            // Insert fish
            val fishItems = listOf(
                "Скумрия" to "🐟", "Пъстърва" to "🐟", "Сьомга" to "🐟", "Риба тон" to "🐟", 
                "Хек" to "🐟", "Калмари" to "🦑", "Скариди" to "🍤", "Миди" to "🦪", 
                "Октопод" to "🐙", "Цаца" to "🐟", "Херинга" to "🐟"
            )
            fishItems.forEach { (name, emoji) ->
                ingredientDao.insertIngredient(Ingredient(name = name, categoryId = fishId.toInt(), emoji = emoji))
            }
            
            // Insert dairy
            val dairyItems = listOf(
                "Прясно мляко" to "🥛", "Кисело мляко" to "🥛", "Сирене (бяло саламурено, краве, овче, козе)" to "🧀", 
                "Кашкавал" to "🧀", "Извара" to "🧀", "Сметана (готварска, сладкарска)" to "🥛", 
                "Маскарпоне" to "🧀", "Рикота" to "🧀", "Яйца (кокоши, пъдпъдъчи)" to "🥚"
            )
            dairyItems.forEach { (name, emoji) ->
                ingredientDao.insertIngredient(Ingredient(name = name, categoryId = dairyId.toInt(), emoji = emoji))
            }
            
            // Insert sweets
            val sweetItems = listOf(
                "Захар (бяла, кафява, пудра)" to "🍬", "Мед" to "🍯", "Меласа" to "🍯", 
                "Шоколад" to "🍫", "Какао" to "🍫", "Сладкарски бои" to "🌈",
                "Ванилова захар" to "🍬", "Бакпулвер" to "⚪", "Сода бикарбонат" to "⚪", 
                "Желатин" to "⚪", "Вафли" to "🧇", "Бисквити" to "🍪",
                "Кондензирано мляко" to "🥛", "Сладко" to "🍯", "Мармалад" to "🍯"
            )
            sweetItems.forEach { (name, emoji) ->
                ingredientDao.insertIngredient(Ingredient(name = name, categoryId = sweetsId.toInt(), emoji = emoji))
            }
            
            // Insert nuts
            val nutItems = listOf(
                "Орехи" to "🌰", "Бадеми" to "🌰", "Лешници" to "🌰", "Фъстъци" to "🥜", 
                "Кашу" to "🥜", "Слънчогледови семки" to "🌻", "Тиквени семки" to "🌱",
                "Чия" to "🌱", "Ленено семе" to "🌱", "Кедрови ядки" to "🌰", "Кокосови стърготини" to "🥥"
            )
            nutItems.forEach { (name, emoji) ->
                ingredientDao.insertIngredient(Ingredient(name = name, categoryId = nutsId.toInt(), emoji = emoji))
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