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
-- Table structure for table `party_user`
--

DROP TABLE IF EXISTS `party_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `party_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `leader` varchar(255) NOT NULL,
  `party_id` bigint DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKoatbt6tv26kk2r5vgua7ojsm` (`party_id`),
  KEY `FKq5094wl2cet4y5vwsq5294uun` (`user_id`),
  CONSTRAINT `FKoatbt6tv26kk2r5vgua7ojsm` FOREIGN KEY (`party_id`) REFERENCES `party` (`id`),
  CONSTRAINT `FKq5094wl2cet4y5vwsq5294uun` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `party_user`
--

LOCK TABLES `party_user` WRITE;
/*!40000 ALTER TABLE `party_user` DISABLE KEYS */;
INSERT INTO `party_user` VALUES (9,'2022-11-16 07:24:23','2022-11-16 07:24:23','N',3,'gq8b3rdetffdi8r3xh755i4gch'),(10,'2022-11-16 07:24:23','2022-11-16 07:24:23','N',3,'q6urk615o3bhxnuditxpuuskta'),(11,'2022-11-16 07:24:23','2022-11-16 07:24:23','N',3,'ugsu4gw5zi859f931so1dhajwy'),(12,'2022-11-16 07:24:23','2022-11-16 07:24:23','N',3,'xonagk335tbk5y484ustmouiie'),(13,'2022-11-16 07:24:23','2022-11-16 07:24:23','N',3,'u6p7w1df9fdh8p6ppxbny6ajay'),(14,'2022-11-16 07:24:23','2022-11-16 07:24:23','Y',3,'7kgcrxmefjyyzg6bi7sfjtuteh'),(15,'2022-11-16 07:26:49','2022-11-16 07:26:49','N',4,'nbob9x3qeigzjfndff83ujt84y'),(16,'2022-11-16 07:26:49','2022-11-16 07:26:49','N',4,'bng71eoy7byxpc4uxa9t1fbmxc'),(17,'2022-11-16 07:26:49','2022-11-16 07:26:49','N',4,'wrs8d9rjsffk88xhoff1o4wwnr'),(18,'2022-11-16 07:26:49','2022-11-16 07:26:49','N',4,'qmrtg6ro8tnuxy8a6eywutb69h'),(19,'2022-11-16 07:26:49','2022-11-16 07:26:49','Y',4,'hwbneo7anifntpqzkydnmd1inw'),(20,'2022-11-16 08:10:10','2022-11-16 08:10:10','N',5,'5s5piq31sp88uqiod5nit7xpee'),(21,'2022-11-16 08:10:10','2022-11-16 08:10:10','N',5,'kpbu4tgucjguppzkw16hmqiakh'),(22,'2022-11-16 08:10:10','2022-11-16 08:10:10','N',5,'yrxcpqnt7jykxma7bjsdgrfcoo'),(23,'2022-11-16 08:10:10','2022-11-16 08:10:10','N',5,'kwp48ombojrgtyah1tbpzgde8y'),(24,'2022-11-16 08:10:10','2022-11-16 08:10:10','Y',5,'87qhbb4apfyudxzh4toii4nj5c'),(25,'2022-11-16 08:40:51','2022-11-16 08:40:51','N',6,'syngmnbiytd8ugp5834doy1gme'),(28,'2022-11-16 08:40:51','2022-11-16 08:40:51','N',6,'pfnfdm4febgd5qmzemdu91ri6w'),(29,'2022-11-16 08:40:51','2022-11-16 08:40:51','N',6,'m1ueqgdkifby7k68arnejn5ayr'),(30,'2022-11-16 08:40:51','2022-11-16 08:40:51','N',6,'wrbeop3scbf9fx4zewgh7d7bka'),(31,'2022-11-16 08:40:51','2022-11-16 08:40:51','Y',6,'mwtyqjzbgif7inf3nwjgwsdu7e'),(32,'2022-11-16 08:45:08','2022-11-16 08:45:08','N',7,'44d94xe4jbbpjc9zrknybmoh9e'),(33,'2022-11-16 08:45:08','2022-11-16 08:45:08','N',7,'6hxy59n163n6pfu1opzws1p9wo'),(34,'2022-11-16 08:45:08','2022-11-16 08:45:08','N',7,'3fgrjrfbebr8uc3o66oext3dsw'),(35,'2022-11-16 08:45:08','2022-11-16 08:45:08','N',7,'qf3w7kj49idtxx6hcirugkua8y'),(36,'2022-11-16 08:45:08','2022-11-16 08:45:08','N',7,'qq5jte5qyjf5fcfyfw759tx3hh'),(37,'2022-11-16 08:45:08','2022-11-16 08:45:08','Y',7,'w1uki85jktngtcyqka59tsqwko'),(38,'2022-11-17 05:51:50','2022-11-17 05:51:50','N',8,'wrbeop3scbf9fx4zewgh7d7bka'),(39,'2022-11-17 05:51:50','2022-11-17 05:51:50','N',8,'m1ueqgdkifby7k68arnejn5ayr'),(40,'2022-11-17 05:51:50','2022-11-17 05:51:50','N',8,'t9qcdsg1nj8h5ki4ppz7nf3i7w'),(41,'2022-11-17 05:51:50','2022-11-17 05:51:50','Y',8,'m9ycjwgt8fgk9cb58u7mpetbdr'),(42,'2022-11-17 06:04:03','2022-11-17 06:04:03','N',9,'jx5oz84p1i8wt84yu6zrmwfbfe'),(43,'2022-11-17 06:04:03','2022-11-17 06:04:03','Y',9,'ifqf5oyyrff4dx7dbnn7r751cc'),(44,'2022-11-17 06:04:19','2022-11-17 06:04:19','N',10,'wrbeop3scbf9fx4zewgh7d7bka'),(45,'2022-11-17 06:04:19','2022-11-17 06:04:19','Y',10,'jx5oz84p1i8wt84yu6zrmwfbfe'),(46,'2022-11-17 06:52:11','2022-11-17 06:52:11','N',11,'wrbeop3scbf9fx4zewgh7d7bka'),(47,'2022-11-17 06:52:11','2022-11-17 06:52:11','Y',11,'bwtzdmegqtygi8spnaw6jk4f4c'),(48,'2022-11-17 07:02:23','2022-11-17 07:02:23','N',12,'kwp48ombojrgtyah1tbpzgde8y'),(49,'2022-11-17 07:02:23','2022-11-17 07:02:23','N',12,'87qhbb4apfyudxzh4toii4nj5c'),(50,'2022-11-17 07:02:23','2022-11-17 07:02:23','Y',12,'yrxcpqnt7jykxma7bjsdgrfcoo'),(51,'2022-11-18 04:34:33','2022-11-18 04:34:33','N',13,'syngmnbiytd8ugp5834doy1gme'),(52,'2022-11-18 04:34:33','2022-11-18 04:34:33','N',13,'wrbeop3scbf9fx4zewgh7d7bka'),(53,'2022-11-18 04:34:33','2022-11-18 04:34:33','N',13,'m1ueqgdkifby7k68arnejn5ayr'),(54,'2022-11-18 04:34:33','2022-11-18 04:34:33','N',13,'pfnfdm4febgd5qmzemdu91ri6w'),(56,'2022-11-18 04:34:33','2022-11-18 04:34:33','Y',13,'okdad9z7tfgh5byfzri9i7eybh'),(59,'2022-11-20 13:27:07','2022-11-20 13:27:07','N',15,'okdad9z7tfgh5byfzri9i7eybh'),(60,'2022-11-20 13:27:07','2022-11-20 13:27:07','Y',15,'wrbeop3scbf9fx4zewgh7d7bka'),(61,'2022-11-20 15:49:42','2022-11-20 15:49:42','N',16,'okdad9z7tfgh5byfzri9i7eybh'),(62,'2022-11-20 15:49:42','2022-11-20 15:49:42','Y',16,'wrbeop3scbf9fx4zewgh7d7bka');
/*!40000 ALTER TABLE `party_user` ENABLE KEYS */;
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

-- Dump completed on 2022-11-21  7:45:20
