create database eazyschool;

use eazyschool;

CREATE TABLE IF NOT EXISTS `contact_msg` (
  `contact_id` int AUTO_INCREMENT  PRIMARY KEY,
  `name` varchar(100) NOT NULL,
  `mobile_num` varchar(10) NOT NULL,
  `email` varchar(100) NOT NULL,
  `subject` varchar(100) NOT NULL,
  `message` varchar(500) NOT NULL,
  `status` varchar(10) NOT NULL,
  `created_at` TIMESTAMP NOT NULL,
  `created_by` varchar(50) NOT NULL,
  `updated_at` TIMESTAMP DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `holidays` (
  `day` varchar(20) NOT NULL,
  `reason` varchar(100) NOT NULL,
  `type` varchar(20) NOT NULL,
  `created_at` TIMESTAMP NOT NULL,
  `created_by` varchar(50) NOT NULL,
  `updated_at` TIMESTAMP DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `roles` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) NOT NULL,
  `created_at` TIMESTAMP NOT NULL,
  `created_by` varchar(50) NOT NULL,
  `updated_at` TIMESTAMP DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
   PRIMARY KEY (`role_id`)
);

CREATE TABLE IF NOT EXISTS `address` (
  `address_id` int NOT NULL AUTO_INCREMENT,
  `address1` varchar(200) NOT NULL,
  `address2` varchar(200) DEFAULT NULL,
  `city` varchar(50) NOT NULL,
  `state` varchar(50) NOT NULL,
  `zip_code` int NOT NULL,
  `created_at` TIMESTAMP NOT NULL,
  `created_by` varchar(50) NOT NULL,
  `updated_at` TIMESTAMP DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
   PRIMARY KEY (`address_id`)
);

CREATE TABLE IF NOT EXISTS `person` (
  `person_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(50) NOT NULL,
  `mobile_number` varchar(20) NOT NULL,
  `pwd` varchar(200) NOT NULL,
  `role_id` int NOT NULL,
  `address_id` int NULL,
  `created_at` TIMESTAMP NOT NULL,
  `created_by` varchar(50) NOT NULL,
  `updated_at` TIMESTAMP DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
   PRIMARY KEY (`person_id`),
   FOREIGN KEY (role_id) REFERENCES roles(role_id),
   FOREIGN KEY (address_id) REFERENCES address(address_id)
);

CREATE TABLE IF NOT EXISTS `class` (
  `class_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `created_at` TIMESTAMP NOT NULL,
  `created_by` varchar(50) NOT NULL,
  `updated_at` TIMESTAMP DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
   PRIMARY KEY (`class_id`)
);

use eazyschool;
ALTER TABLE `class`
ADD COLUMN `teacher_id` int NULL AFTER `name`,
ADD CONSTRAINT `FK_CLASS_TEACHER_ID` FOREIGN KEY (`teacher_id`)
REFERENCES `teacher`(`teacher_id`);

ALTER TABLE `person`
ADD COLUMN `class_id` int NULL AFTER `address_id`,
ADD CONSTRAINT `FK_CLASS_CLASS_ID` FOREIGN KEY (`class_id`)
REFERENCES `class`(`class_id`);

ALTER TABLE `person`
ADD COLUMN `teacher_id` INT NULL AFTER `address_id`,
ADD CONSTRAINT `FK_TEACHER_TEACHER_ID` FOREIGN KEY (`teacher_id`)
REFERENCES `teacher`(`teacher_id`);

CREATE TABLE IF NOT EXISTS `courses` (
  `course_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `fees` varchar(10) NOT NULL,
  `created_at` TIMESTAMP NOT NULL,
  `created_by` varchar(50) NOT NULL,
  `updated_at` TIMESTAMP DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
   PRIMARY KEY (`course_id`)
);

CREATE TABLE IF NOT EXISTS `person_courses` (
  `person_id` int NOT NULL,
  `course_id` int NOT NULL,
  FOREIGN KEY (person_id) REFERENCES person(person_id),
  FOREIGN KEY (course_id) REFERENCES courses(course_id),
   PRIMARY KEY (`person_id`,`course_id`)
);

CREATE TABLE IF NOT EXISTS `teacher_classes` (
  `teacher_id` int NOT NULL,
  `class_id` int NOT NULL,
  FOREIGN KEY (teacher_id) REFERENCES teacher(teacher_id),
  FOREIGN KEY (class_id) REFERENCES class(class_id),
   PRIMARY KEY (`teacher_id`,`class_id`)
);

CREATE TABLE IF NOT EXISTS `question` (
  `question_id` int NOT NULL AUTO_INCREMENT,
  `que` varchar(100) NOT NULL,
  `ans` varchar(10) DEFAULT NULL,
  `person_id` int NOT NULL,
  `teacher_id` int NOT NULL,
  `created_at` TIMESTAMP NOT NULL,
  `created_by` varchar(50) NOT NULL,
  `updated_at` TIMESTAMP DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
   PRIMARY KEY (`question_id`),
   FOREIGN KEY (person_id) REFERENCES person(person_id),
   FOREIGN KEY (teacher_id) REFERENCES teacher(teacher_id)
);

CREATE TABLE IF NOT EXISTS `subjects` (
  `subject_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `teacher_id` INT DEFAULT NULL,
  `created_at` TIMESTAMP NOT NULL,
  `created_by` VARCHAR(50) NOT NULL,
  `updated_at` TIMESTAMP DEFAULT NULL,
  `updated_by` VARCHAR(50) DEFAULT NULL,
   PRIMARY KEY (`subject_id`),
   FOREIGN KEY (teacher_id) REFERENCES teacher(teacher_id)
);

CREATE TABLE IF NOT EXISTS `subjects_classes` (
  `subject_id` INT NOT NULL,
  `class_id` INT NOT NULL,
  FOREIGN KEY (subject_id) REFERENCES subjects(subject_id),
  FOREIGN KEY (class_id) REFERENCES class(class_id),
  PRIMARY KEY (`subject_id`,`class_id`)
);

CREATE TABLE IF NOT EXISTS `leave_requests` (
  `leave_id` INT NOT NULL AUTO_INCREMENT,
  `student_id` INT DEFAULT NULL,
  `teacher_id` INT DEFAULT NULL,
  `reason` VARCHAR(255) NOT NULL,
  `status` VARCHAR(50) NOT NULL,
  `start_date` TIMESTAMP NOT NULL,
  `end_date` TIMESTAMP NOT NULL,
  `request_date` TIMESTAMP NOT NULL,
  `created_at` TIMESTAMP NOT NULL,
  `created_by` VARCHAR(50) NOT NULL,
  `updated_at` TIMESTAMP DEFAULT NULL,
  `updated_by` VARCHAR(50) DEFAULT NULL,
   PRIMARY KEY (`leave_id`),
   FOREIGN KEY (student_id) REFERENCES person(person_id),
   FOREIGN KEY (teacher_id) REFERENCES teacher(teacher_id)
);