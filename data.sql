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
(uuid_generate_v4(), 'maria@example.com', '$argon2id$v=19$m=65536,t=3,p=4$O09OkqGHl74ucu293lNxuw$OgTadbPObj9sc2EZFm0hK4ppzSCb7ro8WtAK3cOQLbg', 'María', 'López');

-- training_level
INSERT INTO training_level (id, level_name, description) VALUES
(uuid_generate_v4(), 'Beginner', 'New to fitness and exercise'),
(uuid_generate_v4(), 'Intermediate', 'Has some experience with fitness routines'),
(uuid_generate_v4(), 'Advanced', 'Highly experienced in fitness and training');

-- fitness_goal
INSERT INTO fitness_goal (id, goal_name, description) VALUES
(uuid_generate_v4(), 'Weight Loss', 'Focus on reducing body fat'),
(uuid_generate_v4(), 'Muscle Gain', 'Focus on increasing muscle mass'),
(uuid_generate_v4(), 'Endurance', 'Improve cardiovascular endurance');

-- dietary_restriction
INSERT INTO dietary_restriction (id, name) VALUES
(uuid_generate_v4(), 'Gluten-Free'),
(uuid_generate_v4(), 'Lactose-Free'),
(uuid_generate_v4(), 'Vegan');

-- dietary_preference
INSERT INTO dietary_preference (id, name) VALUES
(uuid_generate_v4(), 'Low-Carb'),
(uuid_generate_v4(), 'Keto'),
(uuid_generate_v4(), 'Paleo');

-- body_measure
INSERT INTO body_measure (id, height_cm, weight_kg, arms_cm, chest_cm, waist_cm, hips_cm, thighs_cm, calves_cm) VALUES
(uuid_generate_v4(), 170, 68, 30, 95, 80, 90, 55, 35),
(uuid_generate_v4(), 180, 85, 32, 100, 90, 95, 60, 38);

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
 (SELECT id FROM directus_users WHERE email = 'maria@example.com'));

-- customer_goal
INSERT INTO customer_goal (id, user_id, goal_id) VALUES
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'juan@example.com')), (SELECT id FROM fitness_goal WHERE goal_name = 'Weight Loss')),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'maria@example.com')), (SELECT id FROM fitness_goal WHERE goal_name = 'Muscle Gain'));

-- customer_restriction
INSERT INTO customer_restriction (customer_id, restriction_id) VALUES
((SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'juan@example.com')), (SELECT id FROM dietary_restriction WHERE name = 'Gluten-Free')),
((SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'maria@example.com')), (SELECT id FROM dietary_restriction WHERE name = 'Vegan'));

-- customer_preference
INSERT INTO customer_preference (customer_id, preference_id) VALUES
((SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'juan@example.com')), (SELECT id FROM dietary_preference WHERE name = 'Low-Carb')),
((SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'maria@example.com')), (SELECT id FROM dietary_preference WHERE name = 'Keto'));

-- meal_plan
INSERT INTO meal_plan (id, customer_id, name, description, goal, date_created) VALUES
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'juan@example.com')), 'Plan de pérdida de peso', 'Enfoque en reducir calorías', 'Weight Loss', CURRENT_DATE),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'maria@example.com')), 'Plan de ganancia muscular', 'Enfoque en proteínas', 'Muscle Gain', CURRENT_DATE);

-- meal
INSERT INTO meal (id, meal_plan_id, meal_type, name, description, calories, protein_g, carbs_g, fat_g) VALUES
(uuid_generate_v4(), (SELECT id FROM meal_plan WHERE name = 'Plan de pérdida de peso'), 'Breakfast', 'Oatmeal with fruits', 'Healthy breakfast option', 300, 10, 50, 5),
(uuid_generate_v4(), (SELECT id FROM meal_plan WHERE name = 'Plan de ganancia muscular'), 'Lunch', 'Grilled chicken with quinoa', 'High-protein lunch', 500, 40, 30, 15);

-- exercise
INSERT INTO exercise (id, name, description, muscle_groups) VALUES
(uuid_generate_v4(), 'Push-ups', 'Bodyweight exercise for chest and triceps', 'Chest, Triceps'),
(uuid_generate_v4(), 'Squats', 'Lower body strength exercise', 'Legs, Glutes');

-- workout
INSERT INTO workout (id, customer_id, name, type, duration_minutes, difficulty, date_created) VALUES
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'juan@example.com')), 'Full Body Workout', 'Gym', 60, 'Easy', CURRENT_DATE),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'maria@example.com')), 'Running Routine', 'Running', 45, 'Medium', CURRENT_DATE);

-- workout_exercise
INSERT INTO workout_exercise (id, workout_id, exercise_id, sets, reps, is_ai_suggested) VALUES
(uuid_generate_v4(), (SELECT id FROM workout WHERE name = 'Full Body Workout'), (SELECT id FROM exercise WHERE name = 'Push-ups'), 3, 15, FALSE),
(uuid_generate_v4(), (SELECT id FROM workout WHERE name = 'Full Body Workout'), (SELECT id FROM exercise WHERE name = 'Squats'), 4, 12, TRUE);

-- running_workout_detail
INSERT INTO running_workout_detail (id, workout_id, distance_km, estimated_time_minutes, elevation_gain_m, route_name, safety_notes) VALUES
(uuid_generate_v4(), (SELECT id FROM workout WHERE name = 'Running Routine'), 5, 30, 100, 'Park Loop', 'Stay hydrated and wear proper shoes.');

-- workout_session
INSERT INTO workout_session (id, customer_id, workout_id, start_time, end_time, calories_burned, distance_km, average_heart_rate) VALUES
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'juan@example.com')), (SELECT id FROM workout WHERE name = 'Full Body Workout'), 
 '2023-10-01 08:00:00', '2023-10-01 09:00:00', 300, NULL, 120),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'maria@example.com')), (SELECT id FROM workout WHERE name = 'Running Routine'), 
 '2023-10-01 09:30:00', '2023-10-01 10:15:00', 400, 5, 130);

-- completed_exercise
INSERT INTO completed_exercise (id, workout_session_id, exercise_id, sets, reps, rpe) VALUES
(uuid_generate_v4(), (SELECT id FROM workout_session WHERE workout_id = (SELECT id FROM workout WHERE name = 'Full Body Workout')), 
 (SELECT id FROM exercise WHERE name = 'Push-ups'), 3, 15, 7),
(uuid_generate_v4(), (SELECT id FROM workout_session WHERE workout_id = (SELECT id FROM workout WHERE name = 'Full Body Workout')), 
 (SELECT id FROM exercise WHERE name = 'Squats'), 4, 12, 8);

-- progress_photo
INSERT INTO progress_photo (id, customer_id, photo_date, title, image_path) VALUES
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'juan@example.com')), '2023-10-01', 'Week 1 Progress', '/images/juan_week1.jpg'),
(uuid_generate_v4(), (SELECT id FROM customer WHERE user_id = (SELECT id FROM directus_users WHERE email = 'maria@example.com')), '2023-10-01', 'Week 1 Progress', '/images/maria_week1.jpg');