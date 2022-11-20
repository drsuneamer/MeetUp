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
-- Table structure for table `meetup`
--

DROP TABLE IF EXISTS `meetup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meetup` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `color` varchar(255) DEFAULT NULL,
  `is_delete` varchar(255) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `channel_id` varchar(255) DEFAULT NULL,
  `manager_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpyeemqtukx8kkg5cnh051jtqn` (`channel_id`),
  KEY `FKgxchcjcgp9qpqhiluo2sy1slj` (`manager_id`),
  CONSTRAINT `FKgxchcjcgp9qpqhiluo2sy1slj` FOREIGN KEY (`manager_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKpyeemqtukx8kkg5cnh051jtqn` FOREIGN KEY (`channel_id`) REFERENCES `channel` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meetup`
--

LOCK TABLES `meetup` WRITE;
/*!40000 ALTER TABLE `meetup` DISABLE KEYS */;
INSERT INTO `meetup` VALUES (1,'2022-11-16 06:57:59','2022-11-17 08:38:11','#f68686','N','MeetUp 1팀','7f45rtkeu3natgcgkquyekcnzr','tjifxpgtjtrouxeade6kntxhha'),(2,'2022-11-16 06:58:35','2022-11-16 06:58:35','#e38f22','N','MeetUp 3팀','9w74nzrmqfbrifobajnnj17y5r','tjifxpgtjtrouxeade6kntxhha'),(3,'2022-11-16 06:58:58','2022-11-16 06:58:58','#e2e811','N','MeetUp 4팀','z9ynkk81mt89jqwfr5riuukzia','tjifxpgtjtrouxeade6kntxhha'),(4,'2022-11-16 06:59:19','2022-11-17 08:39:14','#61d874','N','MeetUp 5팀','hc818hj8wfnoieombebsmttnfr','tjifxpgtjtrouxeade6kntxhha'),(5,'2022-11-16 06:59:43','2022-11-17 08:38:50','#95aaec','N','MeetUp 6팀','wbgfsau94ty678ndn9bx417umy','tjifxpgtjtrouxeade6kntxhha'),(6,'2022-11-16 07:00:14','2022-11-17 08:38:34','#ffbbbb','N','MeetUp 7팀','fohh39h7cprfmeda8suirzwzqa','tjifxpgtjtrouxeade6kntxhha'),(7,'2022-11-16 07:00:45','2022-11-17 08:37:30','#f191f1','N','MeetUp 8팀','hwx3cdg46prhuxzonwkt9k4jpo','tjifxpgtjtrouxeade6kntxhha'),(8,'2022-11-16 08:07:31','2022-11-16 08:07:31','#ED8383','N','잡담','j3ma4s615ir47q33weuey7pf8o','87qhbb4apfyudxzh4toii4nj5c'),(9,'2022-11-16 08:39:23','2022-11-16 08:39:23','#d4ed83','N','MeetUp! 테스트용 채널','6awee8ddxbfednb17abd9w3uae','mwtyqjzbgif7inf3nwjgwsdu7e'),(10,'2022-11-17 01:51:58','2022-11-17 01:51:58','#ED8383','N','MeetUp! 테스트용 채널','6awee8ddxbfednb17abd9w3uae','fpstcky4mif33dhi9udu3d8e6w'),(11,'2022-11-17 04:53:31','2022-11-17 04:53:31','#fd6363','N','Meet-Up 테스트용 채널','f8n88u4yzfnt7phg9fn5yuw8bw','m1ueqgdkifby7k68arnejn5ayr'),(12,'2022-11-17 05:56:09','2022-11-17 05:56:09','#a0dbef','N','A102','po6x956azif3ibwpwpp9xbq53c','okdad9z7tfgh5byfzri9i7eybh'),(13,'2022-11-17 06:00:15','2022-11-17 06:00:15','#00d6ff','N','2팀 밋업 TEST','po6x956azif3ibwpwpp9xbq53c','syngmnbiytd8ugp5834doy1gme'),(14,'2022-11-17 06:32:53','2022-11-17 06:32:53','#ED8383','N','A502_데일리스크럼','7m1oqozhc38a8fftbyw7j7quqc','y1761jumetnqpehnc411ao5u7y'),(15,'2022-11-17 06:34:37','2022-11-17 06:35:21','#d30808','Y','잡담','kzkwr5cbi7d8zxa3bjp4ue79bh','57w3daz4qfyppmxwwxjk56e8sr'),(16,'2022-11-19 05:00:30','2022-11-19 05:00:56','#7ce6ea','N','A102_TEST','po6x956azif3ibwpwpp9xbq53c','tjifxpgtjtrouxeade6kntxhha'),(17,'2022-11-19 05:01:30','2022-11-19 05:02:07','#edbd83','Y','a102_channel_test','rfqadr7bnfn9xqcbnn5het55nw','tjifxpgtjtrouxeade6kntxhha'),(18,'2022-11-19 05:02:40','2022-11-19 05:19:34','#d54bfd','N','A102_jenkins','efdmeeyw7j81zj68e5uqkar5aa','tjifxpgtjtrouxeade6kntxhha'),(19,'2022-11-19 05:03:29','2022-11-19 05:20:30','#76fdea','N','hello1234','fidienfwrtrjibsuop49zb1jdy','tjifxpgtjtrouxeade6kntxhha'),(20,'2022-11-19 05:04:27','2022-11-19 05:04:27','#9bed83','N','A102_BOT_Test','tzxgwhr7w7ysx8teozkue43fjh','tjifxpgtjtrouxeade6kntxhha');
/*!40000 ALTER TABLE `meetup` ENABLE KEYS */;
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
