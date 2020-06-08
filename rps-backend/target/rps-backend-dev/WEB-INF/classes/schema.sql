-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 23, 2019 at 02:16 PM
-- Server version: 10.1.35-MariaDB
-- PHP Version: 7.2.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `planification`
-- USE `planification`;

-- --------------------------------------------------------

-- DROP TABLES
--
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `calendar`;
DROP TABLE IF EXISTS `parameters`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `members`;
DROP TABLE IF EXISTS `member_position`;
DROP TABLE IF EXISTS `projects`;
DROP TABLE IF EXISTS `project_position`;
SET FOREIGN_KEY_CHECKS = 1;


-- ----------------------------TABLES STRUCTURE--------------------------------

--
-- Table structure for table `calendar`
--
CREATE TABLE `calendar`  (
                           `id` varchar(15) NOT NULL PRIMARY KEY,
                           `fiscal_year` int(11) NOT NULL,
                           `period` int(11) NOT NULL,
                           `week` int(11) NOT NULL,
                           `bop` date NOT NULL,
                           `eop` date NOT NULL,
                           `quarter` varchar(3) NOT NULL,
                           KEY `id_calendar` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `projects`
--
CREATE TABLE `projects` (
                         `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         `project_code` varchar(16) NOT NULL,
                         `project_name` varchar(255) NOT NULL,
                         `id_calendar_start_date_fk` varchar(15) NOT NULL,
                         `id_calendar_end_date_fk` varchar(15) DEFAULT NULL,
                         `status_id` int(11) NOT NULL,
                         `percent_id` int(11) NOT NULL,
                         UNIQUE KEY `project_code` (`project_code`),
                         CONSTRAINT `project_calendar_fk1` FOREIGN KEY (`id_calendar_start_date_fk`) REFERENCES `calendar` (`id`) ON DELETE CASCADE,
                         CONSTRAINT `project_calendar_fk2` FOREIGN KEY (`id_calendar_end_date_fk`) REFERENCES `calendar` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

--
-- Table structure for table `project_position`
--
CREATE TABLE `project_position` (
                                  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                  `id_projects_fk` int(11) NOT NULL,
                                  `position_id` int(11) NOT NULL,
                                  `number_positions` int(11) NOT NULL,
                                  `percent_id` int(11) NOT NULL,
                                  `id_calendar_start_date_fk` varchar(15) NOT NULL,
                                  `id_calendar_end_date_fk` varchar(15) DEFAULT NULL,
                                  CONSTRAINT `project_position_ibfk_1` FOREIGN KEY (`id_projects_fk`) REFERENCES `projects` (`id`) ON DELETE CASCADE,
                                  CONSTRAINT `project_position_ibfk_2` FOREIGN KEY (`id_calendar_start_date_fk`) REFERENCES `calendar` (`id`) ON DELETE CASCADE,
                                  CONSTRAINT `project_position_ibfk_31` FOREIGN KEY (`id_calendar_end_date_fk`) REFERENCES `calendar` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;

--
-- Table structure for table `parameters`
--
CREATE TABLE `parameters` (
                            `type` varchar(20) NOT NULL,
                            `id` int(11) NOT NULL,
                            `description` varchar(255) NOT NULL,
                            CONSTRAINT PRIMARY KEY (`type`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `user`
--
CREATE TABLE `user` (
                      `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                      `email` varchar(255) NOT NULL,
                      `password` varchar(255) NOT NULL,
                      `parola` varchar(255) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Table structure for table `members`
--
CREATE TABLE `members` (
                        `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        `staff_number` varchar(16) NOT NULL,
                        `last_name` varchar(255) NOT NULL,
                        `first_name` varchar(255) NOT NULL,
                        `flag` tinyint(1) DEFAULT NULL,
                        `technology_id` int(11) NOT NULL,
                        `comment` TEXT,
                        UNIQUE KEY `Staff_Number` (`staff_number`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;

--
-- Table structure for table `member_position`
--
CREATE TABLE `member_position` (
                                 `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                 `id_members_fk` int(11) NOT NULL,
                                 `id_project_position_fk` int(11) NOT NULL,
                                 `percent_id` int(11) NOT NULL,
                                 `id_calendar_start_date_fk` varchar(15) NOT NULL,
                                 `id_calendar_end_date_fk` varchar(15) NOT NULL,
                                 CONSTRAINT `members_ibfk_1` FOREIGN KEY (`id_members_fk`) REFERENCES `members` (`id`),
                                 CONSTRAINT `member_position_ibfk_13` FOREIGN KEY (`id_calendar_start_date_fk`) REFERENCES `calendar` (`id`) ON DELETE CASCADE,
                                 CONSTRAINT `member_position_ibfk_14` FOREIGN KEY (`id_calendar_end_date_fk`) REFERENCES `calendar` (`id`) ON DELETE CASCADE,
                                 CONSTRAINT `project_position_ibfk_3` FOREIGN KEY (`id_project_position_fk`) REFERENCES `project_position` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;



-- ------------------------------------VIEWS STRUCTURE-------------------------------------

--
-- Structure for view `member_position_view`
--
CREATE OR REPLACE VIEW `member_position_view` AS select distinct uuid() AS `id`,`mb`.`last_name` AS `last_name`,`mb`.`first_name` AS `first_name`,`pm`.`id_project_position_fk` AS `id_project_position_fk`,(select distinct `parameters`.`description` from `parameters` where ((`parameters`.`type` = 'PERCENT') and (`parameters`.`id` = `pp`.`percent_id`) and (`pp`.`id` = `pm`.`id_project_position_fk`))) AS `percent_position`,(select distinct `parameters`.`description` from `parameters` where ((`parameters`.`type` = 'PERCENT') and (`parameters`.`id` = `pm`.`percent_id`))) AS `percent_member`,(select `parameters`.`description` from `parameters` where ((`parameters`.`type` = 'PERCENT') and (`parameters`.`id` = `prj`.`percent_id`))) AS `percent_project`,(select `parameters`.`description` from `parameters` where ((`parameters`.`type` = 'STATUS') and (`parameters`.`id` = `prj`.`status_id`))) AS `status_project`,`pm`.`id_calendar_start_date_fk` AS `id_calendar_start_date_fk`,`pm`.`id_calendar_end_date_fk` AS `id_calendar_end_date_fk`,(select projects.`project_name` from projects join project_position on projects.`id` = project_position.`id_projects_fk` where project_position.`id` = id_project_position_fk) AS project_name from (((`members` `mb` left join `member_position` `pm` on((`mb`.`id` = `pm`.`id_members_fk`))) left join `project_position` `pp` on((`pp`.`id` = `pm`.`id_project_position_fk`))) left join `projects` `prj` on((`prj`.`id` = `pp`.`id_projects_fk`))) where (`mb`.`flag` = 1) ;

--
-- Structure for view `parameter_percent_view`
--
CREATE OR REPLACE VIEW `parameter_percent_view` AS select `parameters`.`type` AS `type`,`parameters`.`id` AS `id`,`parameters`.`description` AS `description` from `parameters` where (`parameters`.`type` = 'percent') ;

--
-- Structure for view `parameter_position_view`
--
CREATE OR REPLACE VIEW `parameter_position_view` AS select `parameters`.`type` AS `type`,`parameters`.`id` AS `id`,`parameters`.`description` AS `description` from `parameters` where (`parameters`.`type` = 'position') ;

--
-- Structure for view `parameter_status_view`
--
CREATE OR REPLACE VIEW `parameter_status_view`  AS  select `parameters`.`type` AS `type`,`parameters`.`id` AS `id`,`parameters`.`description` AS `description` from `parameters` where (`parameters`.`type` = 'status') ;

--
-- Structure for view `parameter_tech_view`
--
CREATE OR REPLACE VIEW `parameter_tech_view`  AS  select `parameters`.`type` AS `type`,`parameters`.`id` AS `id`,`parameters`.`description` AS `description` from `parameters` where (`parameters`.`type` = 'tech') ;




-- -------------------------------- INITIAL TABLE INSERTS ------------------------------------------


INSERT INTO `calendar` (`id`, `fiscal_year`, `period`, `week`, `bop`, `eop`, `quarter`) VALUES
('FY20-P01-W01', 2020, 1, 1, '2019-09-23', '2019-09-29', 'Q1'),
('FY20-P01-W02', 2020, 1, 2, '2019-09-30', '2019-10-06', 'Q1'),
('FY20-P01-W03', 2020, 1, 3, '2019-10-07', '2019-10-13', 'Q1'),
('FY20-P01-W04', 2020, 1, 4, '2019-10-14', '2019-10-20', 'Q1'),
('FY20-P01-W05', 2020, 1, 5, '2019-10-21', '2019-10-27', 'Q1'),
('FY20-P02-W06', 2020, 2, 6, '2019-10-28', '2019-11-03', 'Q1'),
('FY20-P02-W07', 2020, 2, 7, '2019-11-04', '2019-11-10', 'Q1'),
('FY20-P02-W08', 2020, 2, 8, '2019-11-11', '2019-11-17', 'Q1'),
('FY20-P02-W09', 2020, 2, 9, '2019-11-18', '2019-11-24', 'Q1'),
('FY20-P03-W10', 2020, 3, 10, '2019-11-25', '2019-12-01', 'Q1'),
('FY20-P03-W11', 2020, 3, 11, '2019-12-02', '2019-12-08', 'Q1'),
('FY20-P03-W12', 2020, 3, 12, '2019-12-09', '2019-12-15', 'Q1'),
('FY20-P04-W13', 2020, 4, 13, '2019-12-16', '2019-12-22', 'Q2'),
('FY20-P04-W14', 2020, 4, 14, '2019-12-23', '2019-12-29', 'Q2'),
('FY20-P04-W15', 2020, 4, 15, '2019-12-30', '2020-01-05', 'Q2'),
('FY20-P04-W16', 2020, 4, 16, '2020-01-06', '2020-01-12', 'Q2'),
('FY20-P04-W17', 2020, 4, 17, '2020-01-13', '2020-01-19', 'Q2'),
('FY20-P04-W18', 2020, 4, 18, '2020-01-20', '2020-01-26', 'Q2'),
('FY20-P05-W19', 2020, 5, 19, '2020-01-27', '2020-02-02', 'Q2'),
('FY20-P05-W20', 2020, 5, 20, '2020-02-03', '2020-02-09', 'Q2'),
('FY20-P05-W21', 2020, 5, 21, '2020-02-10', '2020-02-16', 'Q2'),
('FY20-P05-W22', 2020, 5, 22, '2020-02-17', '2020-02-23', 'Q2'),
('FY20-P06-W23', 2020, 6, 23, '2020-02-24', '2020-03-02', 'Q2'),
('FY20-P06-W24', 2020, 6, 24, '2020-03-03', '2020-03-09', 'Q2'),
('FY20-P06-W25', 2020, 6, 25, '2020-03-10', '2020-03-16', 'Q2'),
('FY20-P06-W26', 2020, 6, 26, '2020-03-17', '2020-03-23', 'Q2'),
('FY20-P07-W27', 2020, 7, 27, '2020-03-24', '2020-03-30', 'Q3'),
('FY20-P07-W28', 2020, 7, 28, '2020-03-31', '2020-04-06', 'Q3'),
('FY20-P07-W29', 2020, 7, 29, '2020-04-07', '2020-04-13', 'Q3'),
('FY20-P07-W30', 2020, 7, 30, '2020-04-14', '2020-04-20', 'Q3'),
('FY20-P07-W31', 2020, 7, 31, '2020-04-21', '2020-04-27', 'Q3'),
('FY20-P08-W32', 2020, 8, 32, '2020-04-28', '2020-05-04', 'Q3'),
('FY20-P08-W33', 2020, 8, 33, '2020-05-05', '2020-05-11', 'Q3'),
('FY20-P08-W34', 2020, 8, 34, '2020-05-12', '2020-05-18', 'Q3'),
('FY20-P08-W35', 2020, 8, 35, '2020-05-19', '2020-05-25', 'Q3'),
('FY20-P09-W36', 2020, 9, 36, '2020-05-26', '2020-06-01', 'Q3'),
('FY20-P09-W37', 2020, 9, 37, '2020-06-02', '2020-06-08', 'Q3'),
('FY20-P09-W38', 2020, 9, 38, '2020-06-09', '2020-06-15', 'Q3'),
('FY20-P09-W39', 2020, 9, 39, '2020-06-16', '2020-06-22', 'Q3'),
('FY20-P10-W40', 2020, 10, 40, '2020-06-23', '2020-06-29', 'Q4'),
('FY20-P10-W41', 2020, 10, 41, '2020-06-30', '2020-07-06', 'Q4'),
('FY20-P10-W42', 2020, 10, 42, '2020-07-07', '2020-07-13', 'Q4'),
('FY20-P10-W43', 2020, 10, 43, '2020-07-14', '2020-07-20', 'Q4'),
('FY20-P10-W44', 2020, 10, 44, '2020-07-21', '2020-07-27', 'Q4'),
('FY20-P11-W45', 2020, 11, 45, '2020-07-28', '2020-08-03', 'Q4'),
('FY20-P11-W46', 2020, 11, 46, '2020-08-04', '2020-08-10', 'Q4'),
('FY20-P11-W47', 2020, 11, 47, '2020-08-11', '2020-08-17', 'Q4'),
('FY20-P11-W48', 2020, 11, 48, '2020-08-18', '2020-08-24', 'Q4'),
('FY20-P12-W49', 2020, 12, 49, '2020-08-25', '2020-08-31', 'Q4'),
('FY20-P12-W50', 2020, 12, 50, '2020-09-01', '2020-09-07', 'Q4'),
('FY20-P12-W51', 2020, 12, 51, '2020-09-08', '2020-09-14', 'Q4'),
('FY20-P12-W52', 2020, 12, 52, '2020-09-15', '2020-09-21', 'Q4');

-- -----------------------------------------------------------------------


INSERT INTO `projects` (`id`, `project_code`, `project_name`, `id_calendar_start_date_fk`, `id_calendar_end_date_fk`, `status_id`, `percent_id`) VALUES
(1, 'INTERN-19-001', 'PSA Project Intern', 'FY20-P01-W01', 'FY20-P12-W52', 2, 10),
(2, 'EU431-98584', 'BNF - TMA JAVA JEE', 'FY20-P01-W01', 'FY20-P02-W06', 2, 10),
(3, '43480', 'iTools', 'FY20-P01-W01', 'FY20-P12-W52', 2, 10),
(4, 'PISTE-18-12-001', 'Décathlon Java Team1', 'FY20-P03-W10', 'FY20-P12-W52', 2, 10),
(5, 'PISTE-19-01-001', 'Thalès Digital Factory', 'FY20-P04-W16', 'FY20-P12-W52', 1, 6),
(7, 'PISTE-18-12-002', 'Décathlon Java Team2', 'FY20-P03-W11', 'FY20-P12-W52', 1, 8),
(8, 'PISTE-18-12-003', 'Décathlon Java Team3', 'FY20-P04-W16', 'FY20-P12-W52', 1, 6),
(9, '106428', 'Stela / Total', 'FY20-P01-W05', 'FY20-P04-W15', 2, 10),
(10, '109588', 'EDF', 'FY20-P01-W01', 'FY20-P01-W03', 2, 10),
(11, 'M00000000083008', 'Safran', 'FY20-P01-W01', 'FY20-P01-W02', 3, 10),
(12, '97948', 'CLARINS Salesforce', 'FY20-P01-W02', 'FY20-P12-W52', 2, 10),
(13, 'TEST1', 'TEST1', 'FY20-P01-W01', 'FY20-P04-W15', 3, 10),
(14, 'PISTE-18-12-006', 'Thales Java 23/11', 'FY20-P03-W11', 'FY20-P12-W52', 1, 8),
(15, 'PISTE-18-12-007', 'Thales Servicenow', 'FY20-P03-W11', 'FY20-P12-W52', 1, 9);

-- --------------------------------------------------------

INSERT INTO `project_position` (`id`, `id_projects_fk`, `position_id`, `number_positions`, `percent_id`, `id_calendar_start_date_fk`, `id_calendar_end_date_fk`) VALUES
(1, 2, 1, 1, 10, 'FY20-P01-W01', 'FY20-P02-W07'),
(2, 1, 2, 4, 5, 'FY20-P01-W01', 'FY20-P03-W10'),
(3, 9, 1, 1, 10, 'FY20-P01-W05', 'FY20-P04-W15'),
(4, 9, 1, 1, 10, 'FY20-P02-W08', 'FY20-P04-W15'),
(5, 12, 6, 1, 10, 'FY20-P01-W02', 'FY20-P12-W52'),
(6, 13, 6, 2, 10, 'FY20-P01-W02', 'FY20-P04-W14'),
(7, 12, 6, 1, 10, 'FY20-P04-W16', 'FY20-P12-W52'),
(8, 14, 1, 2, 10, 'FY20-P03-W11', 'FY20-P12-W52'),
(9, 14, 2, 1, 10, 'FY20-P03-W11', 'FY20-P12-W52'),
(10, 14, 7, 1, 10, 'FY20-P03-W11', 'FY20-P12-W52'),
(11, 14, 4, 1, 10, 'FY20-P03-W11', 'FY20-P12-W52'),
(12, 4, 1, 2, 10, 'FY20-P03-W10', 'FY20-P12-W52'),
(13, 4, 2, 1, 10, 'FY20-P03-W10', 'FY20-P12-W52'),
(14, 7, 4, 1, 10, 'FY20-P03-W11', 'FY20-P12-W52'),
(15, 7, 1, 2, 10, 'FY20-P04-W16', 'FY20-P12-W52'),
(16, 3, 1, 1, 10, 'FY20-P01-W01', 'FY20-P12-W52'),
(17, 5, 7, 1, 5, 'FY20-P04-W16', 'FY20-P12-W52'),
(18, 5, 1, 2, 10, 'FY20-P04-W16', 'FY20-P12-W52'),
(19, 5, 1, 2, 10, 'FY20-P06-W23', 'FY20-P12-W52'),
(20, 5, 1, 2, 10, 'FY20-P07-W28', 'FY20-P12-W52'),
(21, 5, 4, 1, 10, 'FY20-P04-W16', 'FY20-P12-W52'),
(22, 5, 10, 1, 10, 'FY20-P05-W19', 'FY20-P12-W52'),
(23, 5, 8, 1, 10, 'FY20-P04-W16', 'FY20-P12-W52'),
(24, 5, 9, 1, 5, 'FY20-P04-W16', 'FY20-P12-W52'),
(25, 5, 11, 3, 10, 'FY20-P04-W16', 'FY20-P12-W52'),
(26, 10, 1, 1, 10, 'FY20-P01-W01', 'FY20-P01-W03'),
(27, 15, 12, 1, 10, 'FY20-P03-W11', 'FY20-P12-W52');

-- --------------------------------------------------------

INSERT INTO `members` (`id`, `staff_number`, `last_name`, `first_name`, `flag`, `technology_id`) VALUES
(1, '229075', 'POPESCU', 'Aura', 1, 5),
(2, '230932', 'ENE', 'Marcela', 1, 5),
(3, '230000', 'STANCU', 'Ionut', 1, 5),
(4, '229931', 'STOIAN', 'Anca', 1, 5),
(6, '235620', 'MITRUT', 'Alexandru', 1, 5),
(7, '238105', 'TUFFREAU', 'Alexis', 1, 5),
(8, '236018', 'MITA', 'Eric', 1, 5),
(9, '238828', 'BOGDAN', 'Bianca', 1, 4),
(10, '228623', 'SANDU', 'Ioana', 1, 5),
(11, 'TMP002', 'DAGO', 'Israel', 1, 5),
(12, '236832', 'BRATOSIN', 'Mihai', 1, 5),
(13, '232945', 'PRICHINDEL', 'Mihai', 1, 5),
(14, '228659', 'SPALATELU', 'Nicolae', 1, 5),
(15, '229103', 'MESESCU', 'Robert', 1, 5),
(16, '235624', 'TOPALA', 'Sorin', 1, 3),
(17, 'TMP003', 'PAUN', 'Cornel', 1, 5),
(20, 'TMP005', 'SPIRIDON', 'Alina', 1, 3),
(21, 'TMP006', 'BETA', 'Gabriela', 1, 5),
(22, 'TMP007', 'POPA', 'Adrian', 1, 5),
(23, 'LPS00317808', 'GUEDON', 'Stephane', 1, 4),
(24, 'TMP008', 'DUMITRESCU', 'Claudiu', 1, 5),
(25, 'TMP009', 'PMO CP JUNIOR', '    ', 1, 4);

-- -------------------------------------------------------------------------

INSERT INTO `member_position` (`id`, `id_members_fk`, `id_project_position_fk`, `percent_id`, `id_calendar_start_date_fk`, `id_calendar_end_date_fk`) VALUES
(9, 13, 3, 10, 'FY20-P01-W05', 'FY20-P04-W15'),
(10, 12, 4, 10, 'FY20-P02-W08', 'FY20-P04-W15'),
(11, 16, 5, 10, 'FY20-P01-W02', 'FY20-P12-W52'),
(12, 2, 6, 10, 'FY20-P01-W02', 'FY20-P04-W14'),
(13, 20, 7, 10, 'FY20-P04-W16', 'FY20-P12-W52'),
(14, 22, 11, 10, 'FY20-P03-W11', 'FY20-P12-W52'),
(15, 1, 8, 10, 'FY20-P03-W11', 'FY20-P12-W52'),
(16, 15, 8, 10, 'FY20-P03-W11', 'FY20-P12-W52'),
(17, 21, 9, 10, 'FY20-P04-W16', 'FY20-P12-W52'),
(18, 2, 12, 10, 'FY20-P03-W10', 'FY20-P12-W52'),
(19, 14, 12, 10, 'FY20-P03-W10', 'FY20-P12-W52'),
(20, 6, 13, 10, 'FY20-P03-W10', 'FY20-P12-W52'),
(21, 24, 14, 10, 'FY20-P03-W11', 'FY20-P12-W52'),
(22, 10, 16, 10, 'FY20-P01-W01', 'FY20-P03-W10'),
(23, 7, 16, 10, 'FY20-P03-W11', 'FY20-P12-W52'),
(24, 23, 17, 4, 'FY20-P04-W16', 'FY20-P12-W52'),
(25, 9, 17, 6, 'FY20-P04-W16', 'FY20-P12-W52'),
(26, 9, 24, 10, 'FY20-P04-W16', 'FY20-P12-W52'),
(27, 9, 23, 10, 'FY20-P04-W16', 'FY20-P12-W52'),
(28, 10, 18, 10, 'FY20-P04-W16', 'FY20-P12-W52'),
(29, 14, 26, 10, 'FY20-P01-W01', 'FY20-P01-W03'),
(30, 25, 10, 10, 'FY20-P04-W16', 'FY20-P12-W52'),
(31, 4, 18, 10, 'FY20-P04-W16', 'FY20-P12-W52'),
(32, 8, 27, 10, 'FY20-P03-W11', 'FY20-P12-W52');

-- --------------------------------------------------------

INSERT INTO `parameters` (`type`, `id`, `description`) VALUES
('PERCENT', 0, '0'),
('PERCENT', 1, '10'),
('PERCENT', 2, '20'),
('PERCENT', 3, '30'),
('PERCENT', 4, '40'),
('PERCENT', 5, '50'),
('PERCENT', 6, '60'),
('PERCENT', 7, '70'),
('PERCENT', 8, '80'),
('PERCENT', 9, '90'),
('PERCENT', 10, '100'),
('POSITION', 1, 'Java Developer'),
('POSITION', 2, 'Junior Java Developer'),
('POSITION', 3, 'Frontend Developer'),
('POSITION', 4, 'Team Lead Java'),
('POSITION', 5, 'Mobile Developer'),
('POSITION', 6, 'Salesforce'),
('POSITION', 7, 'Project Manager'),
('POSITION', 8, 'Product Owner'),
('POSITION', 9, 'Scrum Master'),
('POSITION', 10, 'Devops'),
('POSITION', 11, 'Arya Developer'),
('POSITION', 12, 'Servicenow Developer'),
('STATUS', 0, 'ABANDONED'),
('STATUS', 1, 'IN PROGRESS'),
('STATUS', 2, 'VALIDE'),
('STATUS', 3, 'TERMINATED'),
('TECH', 1, 'DATA'),
('TECH', 2, 'QA'),
('TECH', 3, 'SALESFORCE'),
('TECH', 4, 'MANAGEMENT'),
('TECH', 5, 'JAVA'),
('TECH', 6, 'C');

-- --------------------------------------------------------

INSERT INTO `user` (`id`, `email`, `password`, `parola`) VALUES
(1, 'a', 'a', NULL);

-- ---------------------------------------------------------------


COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
