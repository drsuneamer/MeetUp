-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: meetup.c9odxkbzng3z.ap-northeast-2.rds.amazonaws.com    Database: meetup
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '';

--
-- Table structure for table `meeting`
--

DROP TABLE IF EXISTS `meeting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meeting` (
  `id` bigint NOT NULL,
  `meetup_id` bigint DEFAULT NULL,
  `party_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1mr9ov7g7d1vxg5e2nxifghsw` (`meetup_id`),
  KEY `FKf4xtt3r0iqtqd3fqkjbl0lw8e` (`party_id`),
  CONSTRAINT `FK1mr9ov7g7d1vxg5e2nxifghsw` FOREIGN KEY (`meetup_id`) REFERENCES `meetup` (`id`),
  CONSTRAINT `FKf4xtt3r0iqtqd3fqkjbl0lw8e` FOREIGN KEY (`party_id`) REFERENCES `party` (`id`),
  CONSTRAINT `FKtd0alp710bn4scqsw54thtaol` FOREIGN KEY (`id`) REFERENCES `schedule` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meeting`
--

LOCK TABLES `meeting` WRITE;
/*!40000 ALTER TABLE `meeting` DISABLE KEYS */;
INSERT INTO `meeting` VALUES (3,1,NULL),(4,1,NULL),(5,2,NULL),(7,2,NULL),(9,2,3),(11,4,NULL),(13,1,NULL),(15,1,NULL),(16,1,NULL),(17,1,NULL),(24,7,NULL),(25,5,NULL),(31,5,NULL),(33,5,NULL),(34,5,NULL),(37,5,7),(38,9,NULL),(40,1,4),(43,11,NULL),(47,11,NULL),(49,8,5),(51,11,NULL),(53,11,9),(61,11,8),(63,11,NULL),(68,11,NULL),(69,11,11),(72,11,12),(73,19,NULL),(74,20,NULL),(75,16,NULL),(76,16,NULL),(77,18,NULL),(78,18,NULL),(79,20,NULL),(80,13,6),(81,16,6),(82,16,13),(84,16,NULL),(85,19,16);
/*!40000 ALTER TABLE `meeting` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-11-21  7:45:21
