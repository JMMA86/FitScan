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
    rpe INTEGER,
    weight_kg INTEGER -- NEW: weight moved per set
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

DO $$
DECLARE
    num_training_levels INTEGER := 5;
    num_fitness_goals INTEGER := 25;
    num_dietary_restrictions INTEGER := 25;
    num_dietary_preferences INTEGER := 25;
    num_body_measures INTEGER := 100;
    num_customers INTEGER := 2;
    num_meal_plans_per_customer INTEGER := 25;
    num_meals_per_plan INTEGER := 10;
    num_exercises INTEGER := 50;
    num_workouts_per_customer INTEGER := 5;
    num_workout_exercises_per_workout INTEGER := 5;
    num_workout_sessions_per_workout INTEGER := 500;
    num_completed_exercises_per_session INTEGER := 10;
    num_progress_photos_per_customer INTEGER := 10;
    workout_session_day_offset INTEGER := 30;

    customer_rec RECORD;
    meal_plan_rec RECORD;
    workout_rec RECORD;
    session_rec RECORD;
    user_rec RECORD;
BEGIN
    -- Insert training levels
    FOR i IN 1..num_training_levels LOOP
        INSERT INTO training_level (id, level_name, description)
        VALUES (uuid_generate_v4(), 'Level ' || i, 'Description for Level ' || i);
    END LOOP;
    RAISE NOTICE 'Inserted % training levels', num_training_levels;

    -- Insert fitness goals
    FOR i IN 1..num_fitness_goals LOOP
        INSERT INTO fitness_goal (id, goal_name, description)
        VALUES (uuid_generate_v4(), 'Goal ' || i, 'Description for Goal ' || i);
    END LOOP;
    RAISE NOTICE 'Inserted % fitness goals', num_fitness_goals;

    -- Insert dietary restrictions
    FOR i IN 1..num_dietary_restrictions LOOP
        INSERT INTO dietary_restriction (id, name)
        VALUES (uuid_generate_v4(), 'Restriction ' || i);
    END LOOP;
    RAISE NOTICE 'Inserted % dietary restrictions', num_dietary_restrictions;

    -- Insert dietary preferences
    FOR i IN 1..num_dietary_preferences LOOP
        INSERT INTO dietary_preference (id, name)
        VALUES (uuid_generate_v4(), 'Preference ' || i);
    END LOOP;
    RAISE NOTICE 'Inserted % dietary preferences', num_dietary_preferences;

    -- Insert body measures
    FOR i IN 1..num_body_measures LOOP
        INSERT INTO body_measure (id, height_cm, weight_kg, arms_cm, chest_cm, waist_cm, hips_cm, thighs_cm, calves_cm)
        VALUES (
            uuid_generate_v4(),
            150 + (RANDOM() * 50)::INTEGER,
            50 + (RANDOM() * 50)::INTEGER,
            20 + (RANDOM() * 20)::INTEGER,
            80 + (RANDOM() * 40)::INTEGER,
            60 + (RANDOM() * 30)::INTEGER,
            70 + (RANDOM() * 40)::INTEGER,
            40 + (RANDOM() * 20)::INTEGER,
            30 + (RANDOM() * 10)::INTEGER
        );
    END LOOP;
    RAISE NOTICE 'Inserted % body measures', num_body_measures;

    -- Insert users into directus_users
    FOR i IN 1..num_customers LOOP
        INSERT INTO directus_users (id, email, password, first_name, last_name)
        VALUES (
            uuid_generate_v4(),
            'user' || i || '_' || (RANDOM() * 100000)::INTEGER || '@example.com', -- Ensure unique email
            '$argon2id$v=19$m=65536,t=3,p=4$O09OkqGHl74ucu293lNxuw$OgTadbPObj9sc2EZFm0hK4ppzSCb7ro8WtAK3cOQLbg',
            'FirstName' || i,
            'LastName' || i
        );
    END LOOP;
    RAISE NOTICE 'Inserted % users into directus_users', num_customers;

    -- Insert customers
    FOR user_rec IN (SELECT id FROM directus_users LIMIT num_customers) LOOP
        INSERT INTO customer (id, age, phone, training_level_id, main_goal_id, body_measure_id, user_id)
        VALUES (
            uuid_generate_v4(),
            18 + (RANDOM() * 50)::INTEGER,
            '+123456' || (RANDOM() * 1000000)::INTEGER,
            (SELECT id FROM training_level OFFSET (RANDOM() * (num_training_levels - 1))::INTEGER LIMIT 1),
            (SELECT id FROM fitness_goal OFFSET (RANDOM() * (num_fitness_goals - 1))::INTEGER LIMIT 1),
            (SELECT id FROM body_measure OFFSET (RANDOM() * (num_body_measures - 1))::INTEGER LIMIT 1),
            user_rec.id
        );
    END LOOP;
    RAISE NOTICE 'Inserted % customers', num_customers;

    -- Insert meal plans
    FOR customer_rec IN (SELECT id FROM customer) LOOP
        FOR i IN 1..num_meal_plans_per_customer LOOP
            INSERT INTO meal_plan (id, customer_id, name, description, goal, date_created)
            VALUES (
                uuid_generate_v4(),
                customer_rec.id,
                'Meal Plan ' || i,
                'Description for Meal Plan ' || i,
                'Goal ' || i,
                CURRENT_DATE
            );
        END LOOP;
    END LOOP;
    RAISE NOTICE 'Inserted % meal plans per customer', num_meal_plans_per_customer;

    -- Insert meals
    FOR meal_plan_rec IN (SELECT id FROM meal_plan) LOOP
        FOR i IN 1..num_meals_per_plan LOOP
            INSERT INTO meal (id, meal_plan_id, meal_type, name, description, calories, protein_g, carbs_g, fat_g)
            VALUES (
                uuid_generate_v4(),
                meal_plan_rec.id,
                CASE WHEN i = 1 THEN 'Breakfast' WHEN i = 2 THEN 'Lunch' ELSE 'Dinner' END,
                'Meal ' || i,
                'Description for Meal ' || i,
                200 + (RANDOM() * 300)::INTEGER,
                10 + (RANDOM() * 40)::INTEGER,
                20 + (RANDOM() * 50)::INTEGER,
                5 + (RANDOM() * 20)::INTEGER
            );
        END LOOP;
    END LOOP;
    RAISE NOTICE 'Inserted % meals per plan', num_meals_per_plan;

    -- Insert exercises
    FOR i IN 1..num_exercises LOOP
        INSERT INTO exercise (id, name, description, muscle_groups)
        VALUES (
            uuid_generate_v4(),
            'Exercise ' || i,
            'Description for Exercise ' || i,
            'Muscle Group ' || i
        );
    END LOOP;
    RAISE NOTICE 'Inserted % exercises', num_exercises;

    -- Insert workouts
    FOR customer_rec IN (SELECT id FROM customer) LOOP
        FOR i IN 1..num_workouts_per_customer LOOP
            INSERT INTO workout (id, customer_id, name, type, duration_minutes, difficulty, date_created)
            VALUES (
                uuid_generate_v4(),
                customer_rec.id,
                'Workout ' || i,
                CASE WHEN i % 2 = 0 THEN 'Gym' ELSE 'Running' END,
                30 + (RANDOM() * 60)::INTEGER,
                CASE WHEN i % 3 = 0 THEN 'Hard' WHEN i % 3 = 1 THEN 'Medium' ELSE 'Easy' END,
                CURRENT_DATE
            );
        END LOOP;
    END LOOP;
    RAISE NOTICE 'Inserted % workouts per customer', num_workouts_per_customer;

    -- Insert workout exercises
    FOR workout_rec IN (SELECT id FROM workout) LOOP
        FOR i IN 1..num_workout_exercises_per_workout LOOP
            INSERT INTO workout_exercise (id, workout_id, exercise_id, sets, reps, is_ai_suggested)
            VALUES (
                uuid_generate_v4(),
                workout_rec.id,
                (SELECT id FROM exercise OFFSET (RANDOM() * (num_exercises - 1))::INTEGER LIMIT 1),
                3 + (RANDOM() * 3)::INTEGER,
                8 + (RANDOM() * 12)::INTEGER,
                (RANDOM() > 0.5)
            );
        END LOOP;
    END LOOP;
    RAISE NOTICE 'Inserted % workout exercises per workout', num_workout_exercises_per_workout;
    -- Insert workout sessions
    DECLARE
        base_start_date DATE := CURRENT_DATE - workout_session_day_offset;
    BEGIN
        FOR workout_rec IN (SELECT id, customer_id FROM workout) LOOP
            FOR i IN 1..num_workout_sessions_per_workout LOOP
                DECLARE
                    session_start_date DATE := base_start_date + ((RANDOM() * workout_session_day_offset)::INTEGER);
                BEGIN
                    INSERT INTO workout_session (id, customer_id, workout_id, start_time, end_time, calories_burned, distance_km, average_heart_rate)
                    VALUES (
                        uuid_generate_v4(),
                        workout_rec.customer_id,
                        workout_rec.id,
                        session_start_date + TIME '08:00:00',
                        session_start_date + TIME '08:00:00' + (30 + (RANDOM() * 150)) * INTERVAL '1 minute',
                        200 + (RANDOM() * 300)::INTEGER,
                        CASE WHEN (SELECT type FROM workout WHERE id = workout_rec.id) = 'Running' THEN (RANDOM() * 10)::NUMERIC(5, 2) ELSE NULL END,
                        100 + (RANDOM() * 50)::INTEGER
                    );
                END;
            END LOOP;
        END LOOP;
    END;
    RAISE NOTICE 'Inserted % workout sessions per workout', num_workout_sessions_per_workout;

    FOR session_rec IN (SELECT id FROM workout_session) LOOP
        FOR i IN 1..num_completed_exercises_per_session LOOP
            INSERT INTO completed_exercise (id, workout_session_id, exercise_id, sets, reps, rpe, weight_kg)
            VALUES (
                uuid_generate_v4(),
                session_rec.id,
                (SELECT id FROM exercise OFFSET (RANDOM() * (num_exercises - 1))::INTEGER LIMIT 1),
                3 + (RANDOM() * 3)::INTEGER,
                8 + (RANDOM() * 12)::INTEGER,
                5 + (RANDOM() * 5)::INTEGER,
                10 + (RANDOM() * 90)::INTEGER
            );
        END LOOP;
    END LOOP;
    RAISE NOTICE 'Inserted % completed exercises per session', num_completed_exercises_per_session;

    -- Insert progress photos
    FOR customer_rec IN (SELECT id FROM customer) LOOP
        FOR i IN 1..num_progress_photos_per_customer LOOP
            INSERT INTO progress_photo (id, customer_id, photo_date, title, image_path)
            VALUES (
                uuid_generate_v4(),
                customer_rec.id,
                CURRENT_DATE - (i || ' weeks')::INTERVAL,
                'Progress Photo ' || i,
                '/images/customer_' || customer_rec.id || '_photo_' || i || '.jpg'
            );
        END LOOP;
    END LOOP;
    RAISE NOTICE 'Inserted % progress photos per customer', num_progress_photos_per_customer;
END $$;

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

DELETE FROM directus_users WHERE email = 'juan@example.com' or email = 'maria@example.com';

INSERT INTO directus_users (id, email, password, first_name, last_name)
VALUES 
(uuid_generate_v4(), 'juan@example.com', '$argon2id$v=19$m=65536,t=3,p=4$O09OkqGHl74ucu293lNxuw$OgTadbPObj9sc2EZFm0hK4ppzSCb7ro8WtAK3cOQLbg', 'Juan', 'Pérez'),
(uuid_generate_v4(), 'maria@example.com', '$argon2id$v=19$m=65536,t=3,p=4$O09OkqGHl74ucu293lNxuw$OgTadbPObj9sc2EZFm0hK4ppzSCb7ro8WtAK3cOQLbg', 'María', 'López');