-- =============================
-- ELIMINAR TABLAS EXISTENTES
-- =============================

DROP TABLE IF EXISTS progress_photo CASCADE;
DROP TABLE IF EXISTS completed_exercise CASCADE;
DROP TABLE IF EXISTS workout_session CASCADE;
DROP TABLE IF EXISTS running_workout_detail CASCADE;
DROP TABLE IF EXISTS workout_exercise CASCADE;
DROP TABLE IF EXISTS workout CASCADE;
DROP TABLE IF EXISTS exercise CASCADE;
DROP TABLE IF EXISTS meal CASCADE;
DROP TABLE IF EXISTS meal_plan CASCADE;
DROP TABLE IF EXISTS customer_preference CASCADE;
DROP TABLE IF EXISTS customer_restriction CASCADE;
DROP TABLE IF EXISTS dietary_preference CASCADE;
DROP TABLE IF EXISTS dietary_restriction CASCADE;
DROP TABLE IF EXISTS customer_goal CASCADE;
DROP TABLE IF EXISTS fitness_goal CASCADE;
DROP TABLE IF EXISTS training_level CASCADE;
DROP TABLE IF EXISTS body_measure CASCADE;
DROP TABLE IF EXISTS customer CASCADE;

-- =============================
-- EXTENSIONES
-- =============================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =============================
-- TABLAS INDEPENDIENTES
-- =============================

CREATE TABLE training_level (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    level_name TEXT NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE fitness_goal (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    goal_name TEXT NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE dietary_restriction (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT UNIQUE NOT NULL
);

CREATE TABLE dietary_preference (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT UNIQUE NOT NULL
);

CREATE TABLE body_measure (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    height_cm INTEGER,
    weight_kg INTEGER,
    arms_cm INTEGER,
    chest_cm INTEGER,
    waist_cm INTEGER,
    hips_cm INTEGER,
    thighs_cm INTEGER,
    calves_cm INTEGER
);

-- =============================
-- TABLAS DEPENDIENTES
-- =============================

CREATE TABLE customer (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    age INTEGER CHECK (age > 0),
    phone TEXT, -- Corregido: El tipo INTEGER no tiene sentido aquí
    training_level_id UUID REFERENCES training_level(id) ON DELETE SET NULL,
    main_goal_id UUID REFERENCES fitness_goal(id) ON DELETE SET NULL,
    body_measure_id UUID REFERENCES body_measure(id) ON DELETE SET NULL,
    user_id UUID UNIQUE REFERENCES directus_users(id) ON DELETE CASCADE
);

CREATE TABLE customer_goal (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES customer(id) ON DELETE CASCADE,
    goal_id UUID REFERENCES fitness_goal(id) ON DELETE CASCADE
);

CREATE TABLE customer_restriction (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID REFERENCES customer(id) ON DELETE CASCADE,
    restriction_id UUID REFERENCES dietary_restriction(id) ON DELETE CASCADE
);

CREATE TABLE customer_preference (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID REFERENCES customer(id) ON DELETE CASCADE,
    preference_id UUID REFERENCES dietary_preference(id) ON DELETE CASCADE
);

CREATE TABLE meal_plan (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID REFERENCES customer(id) ON DELETE CASCADE,
    name TEXT,
    description TEXT,
    goal TEXT,
    date_created DATE DEFAULT CURRENT_DATE
);

CREATE TABLE meal (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    meal_plan_id UUID REFERENCES meal_plan(id) ON DELETE CASCADE,
    meal_type TEXT,
    name TEXT,
    description TEXT,
    calories INTEGER,
    protein_g INTEGER,
    carbs_g INTEGER,
    fat_g INTEGER
);

CREATE TABLE exercise (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT NOT NULL,
    description TEXT,
    muscle_groups TEXT
);

CREATE TABLE workout (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID REFERENCES customer(id) ON DELETE CASCADE,
    name TEXT,
    type TEXT CHECK (type IN ('Gym', 'Running')),
    duration_minutes INTEGER,
    difficulty TEXT,
    date_created DATE DEFAULT CURRENT_DATE
);

CREATE TABLE workout_exercise (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    workout_id UUID REFERENCES workout(id) ON DELETE CASCADE,
    exercise_id UUID REFERENCES exercise(id) ON DELETE CASCADE,
    sets INTEGER,
    reps INTEGER,
    is_ai_suggested BOOLEAN DEFAULT FALSE
);

CREATE TABLE running_workout_detail (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    workout_id UUID REFERENCES workout(id) ON DELETE CASCADE,
    distance_km INTEGER,
    estimated_time_minutes INTEGER,
    elevation_gain_m INTEGER,
    route_name TEXT,
    safety_notes TEXT
);

CREATE TABLE workout_session (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID REFERENCES customer(id) ON DELETE CASCADE,
    workout_id UUID REFERENCES workout(id) ON DELETE CASCADE,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    calories_burned INTEGER,
    distance_km INTEGER,
    average_heart_rate INTEGER
);

CREATE TABLE completed_exercise (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    workout_session_id UUID REFERENCES workout_session(id) ON DELETE CASCADE,
    exercise_id UUID REFERENCES exercise(id) ON DELETE CASCADE,
    sets INTEGER,
    reps INTEGER,
    rpe INTEGER
);

CREATE TABLE progress_photo (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID REFERENCES customer(id) ON DELETE CASCADE,
    photo_date DATE,
    title TEXT,
    image_path TEXT
);

-- =============================
-- INSERTAR DATOS DE PRUEBA
-- =============================

-- =============================
-- ELIMINAR DATOS DE TODAS LAS TABLAS
-- =============================

-- TRUNCATE TABLE progress_photo CASCADE;
-- TRUNCATE TABLE completed_exercise CASCADE;
-- TRUNCATE TABLE workout_session CASCADE;
-- TRUNCATE TABLE running_workout_detail CASCADE;
-- TRUNCATE TABLE workout_exercise CASCADE;
-- TRUNCATE TABLE workout CASCADE;
-- TRUNCATE TABLE exercise CASCADE;
-- TRUNCATE TABLE meal CASCADE;
-- TRUNCATE TABLE meal_plan CASCADE;
-- TRUNCATE TABLE customer_preference CASCADE;
-- TRUNCATE TABLE customer_restriction CASCADE;
-- TRUNCATE TABLE customer_goal CASCADE;
-- TRUNCATE TABLE customer CASCADE;
-- TRUNCATE TABLE body_measure CASCADE;
-- TRUNCATE TABLE dietary_preference CASCADE;
-- TRUNCATE TABLE dietary_restriction CASCADE;
-- TRUNCATE TABLE fitness_goal CASCADE;
-- TRUNCATE TABLE training_level CASCADE;

-- =============================
-- TABLAS INDEPENDIENTES
-- =============================

DELETE FROM directus_users WHERE email = 'juan@example.com' or email = 'maria@example.com';

INSERT INTO directus_users (id, email, password, first_name, last_name)
VALUES 
(uuid_generate_v4(), 'juan@example.com', '$argon2id$v=19$m=65536,t=3,p=4$O09OkqGHl74ucu293lNxuw$OgTadbPObj9sc2EZFm0hK4ppzSCb7ro8WtAK3cOQLbg', 'Juan', 'Pérez'),
(uuid_generate_v4(), 'maria@example.com', '$argon2id$v=19$m=65536,t=3,p=4$O09OkqGHl74ucu293lNxuw$OgTadbPObj9sc2EZFm0hK4ppzSCb7ro8WtAK3cOQLbg', 'María', 'López'),
(uuid_generate_v4(), 'carlos@example.com', '$argon2id$v=19$m=65536,t=3,p=4$O09OkqGHl74ucu293lNxuw$OgTadbPObj9sc2EZFm0hK4ppzSCb7ro8WtAK3cOQLbg', 'Carlos', 'Gómez'),
(uuid_generate_v4(), 'ana@example.com', '$argon2id$v=19$m=65536,t=3,p=4$O09OkqGHl74ucu293lNxuw$OgTadbPObj9sc2EZFm0hK4ppzSCb7ro8WtAK3cOQLbg', 'Ana', 'Martínez');

-- training_level
INSERT INTO training_level (id, level_name, description) VALUES
(uuid_generate_v4(), 'Beginner', 'New to fitness and exercise'),
(uuid_generate_v4(), 'Intermediate', 'Has some experience with fitness routines'),
(uuid_generate_v4(), 'Advanced', 'Highly experienced in fitness and training'),
(uuid_generate_v4(), 'Expert', 'Extremely advanced fitness level'),
(uuid_generate_v4(), 'Professional', 'Elite athlete level');

-- fitness_goal
INSERT INTO fitness_goal (id, goal_name, description) VALUES
(uuid_generate_v4(), 'Weight Loss', 'Focus on reducing body fat'),
(uuid_generate_v4(), 'Muscle Gain', 'Focus on increasing muscle mass'),
(uuid_generate_v4(), 'Endurance', 'Improve cardiovascular endurance'),
(uuid_generate_v4(), 'Flexibility', 'Improve range of motion and flexibility'),
(uuid_generate_v4(), 'Strength', 'Focus on increasing physical strength');

-- dietary_restriction
INSERT INTO dietary_restriction (id, name) VALUES
(uuid_generate_v4(), 'Gluten-Free'),
(uuid_generate_v4(), 'Lactose-Free'),
(uuid_generate_v4(), 'Vegan'),
(uuid_generate_v4(), 'Nut-Free'),
(uuid_generate_v4(), 'Vegetarian');

-- dietary_preference
INSERT INTO dietary_preference (id, name) VALUES
(uuid_generate_v4(), 'Low-Carb'),
(uuid_generate_v4(), 'Keto'),
(uuid_generate_v4(), 'Paleo'),
(uuid_generate_v4(), 'Mediterranean'),
(uuid_generate_v4(), 'High-Protein');

-- body_measure
INSERT INTO body_measure (id, height_cm, weight_kg, arms_cm, chest_cm, waist_cm, hips_cm, thighs_cm, calves_cm) VALUES
(uuid_generate_v4(), 170, 68, 30, 95, 80, 90, 55, 35),
(uuid_generate_v4(), 180, 85, 32, 100, 90, 95, 60, 38),
(uuid_generate_v4(), 165, 60, 28, 90, 75, 85, 50, 33),
(uuid_generate_v4(), 175, 78, 31, 98, 85, 92, 58, 36);

-- =============================
-- TABLAS DEPENDIENTES
-- =============================

-- customer
INSERT INTO customer (id, age, phone, training_level_id, main_goal_id, body_measure_id, user_id) VALUES
(uuid_generate_v4(), 28, '+123456789', 
 (SELECT id FROM training_level WHERE level_name = 'Beginner'), 
 (SELECT id FROM fitness_goal WHERE goal_name = 'Weight Loss'), 
 (SELECT id FROM body_measure LIMIT 1), 
 (SELECT id FROM directus_users WHERE email = 'juan@example.com')),
(uuid_generate_v4(), 35, '+987654321', 
 (SELECT id FROM training_level WHERE level_name = 'Intermediate'), 
 (SELECT id FROM fitness_goal WHERE goal_name = 'Muscle Gain'), 
 (SELECT id FROM body_measure LIMIT 1 OFFSET 1), 
 (SELECT id FROM directus_users WHERE email = 'maria@example.com')),
(uuid_generate_v4(), 30, '+1122334455', 
 (SELECT id FROM training_level WHERE level_name = 'Expert'), 
 (SELECT id FROM fitness_goal WHERE goal_name = 'Strength'), 
 (SELECT id FROM body_measure LIMIT 1 OFFSET 2), 
 (SELECT id FROM directus_users WHERE email = 'carlos@example.com')),
(uuid_generate_v4(), 25, '+9988776655', 
 (SELECT id FROM training_level WHERE level_name = 'Professional'), 
 (SELECT id FROM fitness_goal WHERE goal_name = 'Flexibility'), 
 (SELECT id FROM body_measure LIMIT 1 OFFSET 3), 
 (SELECT id FROM directus_users WHERE email = 'ana@example.com'));

-- customer_goal
INSERT INTO customer_goal (id, user_id, goal_id) VALUES
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'juan@example.com')), (SELECT id FROM fitness_goal WHERE goal_name = 'Weight Loss')),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'maria@example.com')), (SELECT id FROM fitness_goal WHERE goal_name = 'Muscle Gain')),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'carlos@example.com')), (SELECT id FROM fitness_goal WHERE goal_name = 'Strength')),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'ana@example.com')), (SELECT id FROM fitness_goal WHERE goal_name = 'Flexibility'));

-- customer_restriction
INSERT INTO customer_restriction (customer_id, restriction_id) VALUES
((SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'juan@example.com')), (SELECT id FROM dietary_restriction WHERE name = 'Gluten-Free')),
((SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'maria@example.com')), (SELECT id FROM dietary_restriction WHERE name = 'Vegan')),
((SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'carlos@example.com')), (SELECT id FROM dietary_restriction WHERE name = 'Nut-Free')),
((SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'ana@example.com')), (SELECT id FROM dietary_restriction WHERE name = 'Vegetarian'));

-- customer_preference
INSERT INTO customer_preference (customer_id, preference_id) VALUES
((SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'juan@example.com')), (SELECT id FROM dietary_preference WHERE name = 'Low-Carb')),
((SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'maria@example.com')), (SELECT id FROM dietary_preference WHERE name = 'Keto')),
((SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'carlos@example.com')), (SELECT id FROM dietary_preference WHERE name = 'Mediterranean')),
((SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'ana@example.com')), (SELECT id FROM dietary_preference WHERE name = 'High-Protein'));

-- meal_plan
INSERT INTO meal_plan (id, customer_id, name, description, goal, date_created) VALUES
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'juan@example.com')), 'Plan de pérdida de peso', 'Enfoque en reducir calorías', 'Weight Loss', CURRENT_DATE),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'maria@example.com')), 'Plan de ganancia muscular', 'Enfoque en proteínas', 'Muscle Gain', CURRENT_DATE),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'carlos@example.com')), 'Strength Plan', 'Focus on high protein intake', 'Strength', CURRENT_DATE),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'ana@example.com')), 'Flexibility Plan', 'Focus on balanced nutrition', 'Flexibility', CURRENT_DATE),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'juan@example.com')), 'High Protein Plan', 'Focus on muscle recovery and growth', 'Muscle Gain', CURRENT_DATE),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'maria@example.com')), 'Balanced Diet Plan', 'Focus on maintaining weight and health', 'Endurance', CURRENT_DATE),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'carlos@example.com')), 'Low Carb Plan', 'Focus on reducing carbohydrate intake', 'Weight Loss', CURRENT_DATE),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'ana@example.com')), 'Vegetarian Plan', 'Focus on plant-based nutrition', 'Flexibility', CURRENT_DATE);

-- meal
INSERT INTO meal (id, meal_plan_id, meal_type, name, description, calories, protein_g, carbs_g, fat_g) VALUES
(uuid_generate_v4(), (SELECT id FROM meal_plan WHERE name = 'Plan de pérdida de peso'), 'Breakfast', 'Oatmeal with fruits', 'Healthy breakfast option', 300, 10, 50, 5),
(uuid_generate_v4(), (SELECT id FROM meal_plan WHERE name = 'Plan de ganancia muscular'), 'Lunch', 'Grilled chicken with quinoa', 'High-protein lunch', 500, 40, 30, 15),
(uuid_generate_v4(), (SELECT id FROM meal_plan WHERE name = 'Strength Plan'), 'Dinner', 'Steak with vegetables', 'High-protein dinner', 600, 50, 20, 25),
(uuid_generate_v4(), (SELECT id FROM meal_plan WHERE name = 'Flexibility Plan'), 'Snack', 'Fruit salad', 'Light and refreshing', 200, 2, 50, 1),
(uuid_generate_v4(), (SELECT id FROM meal_plan WHERE name = 'High Protein Plan'), 'Breakfast', 'Egg Whites and Spinach', 'High-protein breakfast', 250, 30, 5, 10),
(uuid_generate_v4(), (SELECT id FROM meal_plan WHERE name = 'Balanced Diet Plan'), 'Lunch', 'Grilled Salmon with Veggies', 'Balanced meal with healthy fats', 400, 35, 20, 15),
(uuid_generate_v4(), (SELECT id FROM meal_plan WHERE name = 'Low Carb Plan'), 'Dinner', 'Zucchini Noodles with Chicken', 'Low-carb dinner option', 350, 40, 10, 12),
(uuid_generate_v4(), (SELECT id FROM meal_plan WHERE name = 'Vegetarian Plan'), 'Snack', 'Hummus with Carrot Sticks', 'Plant-based snack', 150, 5, 20, 8),
(uuid_generate_v4(), (SELECT id FROM meal_plan WHERE name = 'High Protein Plan'), 'Snack', 'Protein Shake', 'Quick recovery drink', 200, 25, 10, 5),
(uuid_generate_v4(), (SELECT id FROM meal_plan WHERE name = 'Balanced Diet Plan'), 'Breakfast', 'Greek Yogurt with Granola', 'Balanced breakfast option', 300, 15, 40, 8),
(uuid_generate_v4(), (SELECT id FROM meal_plan WHERE name = 'Low Carb Plan'), 'Lunch', 'Grilled Steak with Asparagus', 'Low-carb lunch', 450, 50, 5, 20),
(uuid_generate_v4(), (SELECT id FROM meal_plan WHERE name = 'Vegetarian Plan'), 'Dinner', 'Quinoa Salad with Chickpeas', 'Plant-based dinner', 400, 15, 50, 10);

-- exercise
INSERT INTO exercise (id, name, description, muscle_groups) VALUES
(uuid_generate_v4(), 'Push-ups', 'Bodyweight exercise for chest and triceps', 'Chest, Triceps'),
(uuid_generate_v4(), 'Squats', 'Lower body strength exercise', 'Legs, Glutes'),
(uuid_generate_v4(), 'Deadlift', 'Strength exercise for the entire body', 'Back, Legs'),
(uuid_generate_v4(), 'Plank', 'Core stability exercise', 'Core'),
(uuid_generate_v4(), 'Bench Press', 'Chest strength exercise', 'Chest, Triceps'),
(uuid_generate_v4(), 'Pull-ups', 'Upper body strength exercise', 'Back, Biceps'),
(uuid_generate_v4(), 'Lunges', 'Lower body exercise for balance and strength', 'Legs, Glutes'),
(uuid_generate_v4(), 'Bicep Curls', 'Arm isolation exercise', 'Biceps'),
(uuid_generate_v4(), 'Tricep Dips', 'Bodyweight exercise for triceps', 'Triceps'),
(uuid_generate_v4(), 'Leg Press', 'Lower body strength exercise', 'Legs'),
(uuid_generate_v4(), 'Shoulder Press', 'Strength exercise for shoulders', 'Shoulders'),
(uuid_generate_v4(), 'Plank to Push-up', 'Core and upper body exercise', 'Core, Chest, Triceps'),
(uuid_generate_v4(), 'Mountain Climbers', 'Cardio and core exercise', 'Core, Legs'),
(uuid_generate_v4(), 'Burpees', 'Full-body cardio exercise', 'Full Body');

-- workout
INSERT INTO workout (id, customer_id, name, type, duration_minutes, difficulty, date_created) VALUES
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'juan@example.com')), 'Full Body Workout', 'Gym', 60, 'Easy', CURRENT_DATE),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'maria@example.com')), 'Running Routine', 'Running', 45, 'Medium', CURRENT_DATE),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'carlos@example.com')), 'Strength Training', 'Gym', 75, 'Hard', CURRENT_DATE),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'ana@example.com')), 'Yoga Session', 'Gym', 60, 'Easy', CURRENT_DATE),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'juan@example.com')), 'Chest Day', 'Gym', 60, 'Medium', CURRENT_DATE),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'maria@example.com')), 'Cardio Blast', 'Running', 45, 'Hard', CURRENT_DATE),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'carlos@example.com')), 'Leg Day', 'Gym', 75, 'Hard', CURRENT_DATE),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'ana@example.com')), 'Yoga Flow', 'Gym', 60, 'Easy', CURRENT_DATE);

-- workout_exercise
INSERT INTO workout_exercise (id, workout_id, exercise_id, sets, reps, is_ai_suggested) VALUES
(uuid_generate_v4(), (SELECT id FROM workout WHERE name = 'Full Body Workout'), (SELECT id FROM exercise WHERE name = 'Push-ups'), 3, 15, FALSE),
(uuid_generate_v4(), (SELECT id FROM workout WHERE name = 'Full Body Workout'), (SELECT id FROM exercise WHERE name = 'Squats'), 4, 12, TRUE),
(uuid_generate_v4(), (SELECT id FROM workout WHERE name = 'Strength Training'), (SELECT id FROM exercise WHERE name = 'Deadlift'), 4, 10, TRUE),
(uuid_generate_v4(), (SELECT id FROM workout WHERE name = 'Yoga Session'), (SELECT id FROM exercise WHERE name = 'Plank'), 3, 60, FALSE),
(uuid_generate_v4(), (SELECT id FROM workout WHERE name = 'Chest Day'), (SELECT id FROM exercise WHERE name = 'Bench Press'), 4, 12, TRUE),
(uuid_generate_v4(), (SELECT id FROM workout WHERE name = 'Chest Day'), (SELECT id FROM exercise WHERE name = 'Push-ups'), 3, 15, FALSE),
(uuid_generate_v4(), (SELECT id FROM workout WHERE name = 'Leg Day'), (SELECT id FROM exercise WHERE name = 'Squats'), 4, 10, TRUE),
(uuid_generate_v4(), (SELECT id FROM workout WHERE name = 'Leg Day'), (SELECT id FROM exercise WHERE name = 'Lunges'), 3, 12, FALSE),
(uuid_generate_v4(), (SELECT id FROM workout WHERE name = 'Cardio Blast'), (SELECT id FROM exercise WHERE name = 'Mountain Climbers'), 5, 30, TRUE),
(uuid_generate_v4(), (SELECT id FROM workout WHERE name = 'Yoga Flow'), (SELECT id FROM exercise WHERE name = 'Plank'), 3, 60, FALSE);

-- running_workout_detail
INSERT INTO running_workout_detail (id, workout_id, distance_km, estimated_time_minutes, elevation_gain_m, route_name, safety_notes) VALUES
(uuid_generate_v4(), (SELECT id FROM workout WHERE name = 'Running Routine'), 5, 30, 100, 'Park Loop', 'Stay hydrated and wear proper shoes.'),
(uuid_generate_v4(), (SELECT id FROM workout WHERE name = 'Yoga Session'), 3, 20, 50, 'City Park', 'Avoid traffic areas.');

-- workout_session
INSERT INTO workout_session (id, customer_id, workout_id, start_time, end_time, calories_burned, distance_km, average_heart_rate) VALUES
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'juan@example.com')), (SELECT id FROM workout WHERE name = 'Full Body Workout'), 
 '2023-10-01 08:00:00', '2023-10-01 09:00:00', 300, NULL, 120),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'maria@example.com')), (SELECT id FROM workout WHERE name = 'Running Routine'), 
 '2023-10-01 09:30:00', '2023-10-01 10:15:00', 400, 5, 130),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'carlos@example.com')), (SELECT id FROM workout WHERE name = 'Strength Training'), 
 '2023-10-02 07:00:00', '2023-10-02 08:15:00', 500, NULL, 140),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'ana@example.com')), (SELECT id FROM workout WHERE name = 'Yoga Session'), 
 '2023-10-02 09:00:00', '2023-10-02 10:00:00', 250, NULL, 100),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'juan@example.com')), (SELECT id FROM workout WHERE name = 'Chest Day'), 
 '2023-10-03 07:00:00', '2023-10-03 08:00:00', 400, NULL, 130),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'maria@example.com')), (SELECT id FROM workout WHERE name = 'Cardio Blast'), 
 '2023-10-03 09:00:00', '2023-10-03 09:45:00', 500, 6, 140),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'carlos@example.com')), (SELECT id FROM workout WHERE name = 'Leg Day'), 
 '2023-10-04 07:30:00', '2023-10-04 08:45:00', 600, NULL, 150),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'ana@example.com')), (SELECT id FROM workout WHERE name = 'Yoga Flow'), 
 '2023-10-04 10:00:00', '2023-10-04 11:00:00', 200, NULL, 90);

DO $$
DECLARE
    start_date DATE := '2024-05-06';
    end_date DATE := '2025-05-06';
    current_loop_date DATE := start_date;
    current_customer_id UUID := (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'juan@example.com'));
    workout_id UUID := (SELECT id FROM workout WHERE workout.customer_id = current_customer_id LIMIT 1); -- Explicitly qualify the column
BEGIN
    WHILE current_loop_date <= end_date LOOP
        INSERT INTO workout_session (
            id, customer_id, workout_id, start_time, end_time, calories_burned, distance_km, average_heart_rate
        ) VALUES (
            uuid_generate_v4(),
            current_customer_id,
            workout_id,
            current_loop_date + TIME '08:00:00',
            current_loop_date + TIME '09:00:00',
            300 + (RANDOM() * 100)::INTEGER,
            (RANDOM() * 5)::NUMERIC(5, 2),
            100 + (RANDOM() * 30)::INTEGER
        );
        current_loop_date := current_loop_date + INTERVAL '3 days';
    END LOOP;
END $$;

-- completed_exercise
INSERT INTO completed_exercise (id, workout_session_id, exercise_id, sets, reps, rpe) VALUES
(uuid_generate_v4(), (SELECT id FROM workout_session WHERE workout_id = (SELECT id FROM workout WHERE name = 'Full Body Workout')), 
 (SELECT id FROM exercise WHERE name = 'Push-ups'), 3, 15, 7),
(uuid_generate_v4(), (SELECT id FROM workout_session WHERE workout_id = (SELECT id FROM workout WHERE name = 'Full Body Workout')), 
 (SELECT id FROM exercise WHERE name = 'Squats'), 4, 12, 8),
(uuid_generate_v4(), (SELECT id FROM workout_session WHERE workout_id = (SELECT id FROM workout WHERE name = 'Strength Training')), 
 (SELECT id FROM exercise WHERE name = 'Deadlift'), 4, 10, 9),
(uuid_generate_v4(), (SELECT id FROM workout_session WHERE workout_id = (SELECT id FROM workout WHERE name = 'Yoga Session')), 
 (SELECT id FROM exercise WHERE name = 'Plank'), 3, 60, 6);

-- progress_photo
INSERT INTO progress_photo (id, customer_id, photo_date, title, image_path) VALUES
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'juan@example.com')), '2023-10-01', 'Week 1 Progress', '/images/juan_week1.jpg'),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'maria@example.com')), '2023-10-01', 'Week 1 Progress', '/images/maria_week1.jpg'),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'carlos@example.com')), '2023-10-02', 'Week 1 Progress', '/images/carlos_week1.jpg'),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'ana@example.com')), '2023-10-02', 'Week 1 Progress', '/images/ana_week1.jpg'),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'juan@example.com')), '2023-10-03', 'Week 2 Progress', '/images/juan_week2.jpg'),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'maria@example.com')), '2023-10-03', 'Week 2 Progress', '/images/maria_week2.jpg'),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'carlos@example.com')), '2023-10-04', 'Week 2 Progress', '/images/carlos_week2.jpg'),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'ana@example.com')), '2023-10-04', 'Week 2 Progress', '/images/ana_week2.jpg');
