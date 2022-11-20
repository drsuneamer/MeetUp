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
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule` (
  `type` varchar(31) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `end` timestamp NULL DEFAULT NULL,
  `is_open` varchar(255) NOT NULL,
  `start` timestamp NULL DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdn5svbxyacce1gpfiawk7iqtc` (`user_id`),
  CONSTRAINT `FKdn5svbxyacce1gpfiawk7iqtc` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
INSERT INTO `schedule` VALUES ('Meeting',3,'2022-11-16 07:19:38','2022-11-16 07:20:16','통풍환자를 포함해서 맛있게 회식을 하자','2022-11-21 00:00:00','Y','2022-11-20 21:00:00','자율팀 회식','nbob9x3qeigzjfndff83ujt84y'),('Meeting',4,'2022-11-16 07:21:50','2022-11-16 08:04:53','점심 별로다.','2022-11-19 03:30:00','N','2022-11-19 02:30:00','점심비판하기','bng71eoy7byxpc4uxa9t1fbmxc'),('Meeting',5,'2022-11-16 07:22:07','2022-11-16 07:22:15','회식 좋다','2022-11-21 13:00:00','Y','2022-11-21 09:00:00','자율3팀 회식','7kgcrxmefjyyzg6bi7sfjtuteh'),('Meeting',7,'2022-11-16 07:26:02','2022-11-16 07:28:29','안돼 돌아가','2022-11-21 13:30:00','Y','2022-11-21 13:00:00','안돼 돌아가','ugsu4gw5zi859f931so1dhajwy'),('Schedule',8,'2022-11-16 07:27:58','2022-11-17 05:58:11',NULL,'2022-11-17 12:00:00','N','2022-11-17 08:30:00','ㅇㄹㄹㅇㄹ','hwbneo7anifntpqzkydnmd1inw'),('Meeting',9,'2022-11-16 07:31:34','2022-11-16 07:31:34','우리 집에는 가자','2022-11-21 14:00:00','Y','2022-11-21 13:30:00','막차시간 찾아보기','u6p7w1df9fdh8p6ppxbny6ajay'),('Meeting',11,'2022-11-16 07:33:39','2022-11-16 07:33:39','테스트해봅니다... 화이팅하세요','2022-11-17 06:00:00','N','2022-11-17 05:30:00','안녕하세요','wi9q1q4imfdizkb7hakbjxq5xe'),('Schedule',12,'2022-11-16 07:58:50','2022-11-16 07:58:50',NULL,'2022-11-16 19:30:00','N','2022-11-16 19:00:00','일기','87qhbb4apfyudxzh4toii4nj5c'),('Meeting',13,'2022-11-16 08:11:22','2022-11-16 08:11:22','','2022-11-21 21:30:00','N','2022-11-21 21:00:00','ㅇㅇㅇㅇ','nbob9x3qeigzjfndff83ujt84y'),('Meeting',15,'2022-11-16 08:12:49','2022-11-16 08:12:49','','2022-11-21 22:00:00','N','2022-11-21 21:30:00','ㅌㅌㅌㅌㅌㅌㅌㅌㅌ','nbob9x3qeigzjfndff83ujt84y'),('Meeting',16,'2022-11-16 08:12:49','2022-11-16 08:12:49','ㄷㅅㅅ','2022-11-21 22:00:00','N','2022-11-21 21:30:00','ㄷㅅㅅ','bng71eoy7byxpc4uxa9t1fbmxc'),('Meeting',17,'2022-11-16 08:13:29','2022-11-16 08:13:29','ㄷㅅㅅ','2022-11-22 21:30:00','Y','2022-11-22 21:00:00','ㄷㅅㅅㅅ','bng71eoy7byxpc4uxa9t1fbmxc'),('Schedule',19,'2022-11-16 08:23:05','2022-11-16 08:23:05',NULL,'2022-11-21 01:00:00','N','2022-11-21 00:30:00','발표','kappsc6jgpymzcegq3izg6du7c'),('Schedule',21,'2022-11-16 08:29:38','2022-11-16 08:29:38',NULL,'2022-11-16 10:00:00','N','2022-11-16 09:00:00','귀가','5hy6rdq1xp8ztbw8kwejyoo5fc'),('Schedule',22,'2022-11-16 08:30:01','2022-11-16 08:30:01',NULL,'2022-11-16 11:00:00','N','2022-11-16 10:00:00','저녁 식사 및 일일 코노 할당량 채우기','5hy6rdq1xp8ztbw8kwejyoo5fc'),('Schedule',23,'2022-11-16 08:32:20','2022-11-16 08:32:20',NULL,'2022-11-16 10:30:00','N','2022-11-16 10:00:00','오늘 저녁 삼겹살','e1s1f4s53irwinq8qu4wpfgouc'),('Meeting',24,'2022-11-16 08:36:49','2022-11-16 08:36:49','분주하게 돌아다니는 명석이','2022-11-17 09:00:00','N','2022-11-17 08:30:00','명석이 안녕','e1s1f4s53irwinq8qu4wpfgouc'),('Meeting',25,'2022-11-16 08:40:46','2022-11-16 08:40:46','미팅을합시다','2022-11-17 00:30:00','N','2022-11-17 00:00:00','미팅하기','44d94xe4jbbpjc9zrknybmoh9e'),('Schedule',26,'2022-11-16 08:42:21','2022-11-16 08:42:21',NULL,'2022-11-17 04:30:00','N','2022-11-17 04:00:00','UCC 찍기','44d94xe4jbbpjc9zrknybmoh9e'),('Schedule',27,'2022-11-16 08:42:27','2022-11-16 08:42:27',NULL,'2022-11-16 22:30:00','N','2022-11-16 15:00:00','쉿 - 경원이는 취침 중','dgb7ehc4nfroxe4cpab1ycamay'),('Schedule',28,'2022-11-16 08:42:46','2022-11-16 08:42:46',NULL,'2022-11-23 17:00:00','N','2022-11-23 16:30:00','공가','3fgrjrfbebr8uc3o66oext3dsw'),('Schedule',29,'2022-11-16 08:42:55','2022-11-16 08:42:55',NULL,'2022-11-17 01:00:00','N','2022-11-17 00:30:00','쉿- 경원이는 스크럼 중','dgb7ehc4nfroxe4cpab1ycamay'),('Schedule',30,'2022-11-16 08:42:58','2022-11-16 08:42:58',NULL,'2022-11-17 05:00:00','N','2022-11-17 04:30:00','홀홀호','44d94xe4jbbpjc9zrknybmoh9e'),('Meeting',31,'2022-11-16 08:43:10','2022-11-16 08:43:10','화이팅하세요~','2022-11-25 07:30:00','Y','2022-11-25 07:00:00','안녕하세요','w1uki85jktngtcyqka59tsqwko'),('Schedule',32,'2022-11-16 08:43:14','2022-11-16 08:43:14',NULL,'2022-11-16 20:00:00','Y','2022-11-16 19:30:00','1반 2팀 화이팅~','6hxy59n163n6pfu1opzws1p9wo'),('Meeting',33,'2022-11-16 08:43:59','2022-11-16 08:43:59','냠ㄴ냠','2022-11-18 00:00:00','N','2022-11-17 23:30:00','밥약속','3fgrjrfbebr8uc3o66oext3dsw'),('Meeting',34,'2022-11-16 08:45:15','2022-11-16 08:45:15','주말 미팅도 받아주시나요?','2022-11-19 08:30:00','Y','2022-11-19 08:00:00','주말 미팅 받아주시나요?','qq5jte5qyjf5fcfyfw759tx3hh'),('Schedule',35,'2022-11-16 08:45:30','2022-11-16 08:46:49',NULL,'2022-11-18 06:30:00','Y','2022-11-17 20:00:00','안녕하세욥','6hxy59n163n6pfu1opzws1p9wo'),('Schedule',36,'2022-11-16 08:47:06','2022-11-16 08:47:06',NULL,'2022-11-17 07:00:00','Y','2022-11-17 06:30:00','ㅇㅈㅇㅈㅇ','6hxy59n163n6pfu1opzws1p9wo'),('Meeting',37,'2022-11-16 08:47:19','2022-11-16 08:47:19','','2022-11-18 11:30:00','Y','2022-11-18 11:00:00','사랑합니다','6hxy59n163n6pfu1opzws1p9wo'),('Meeting',38,'2022-11-16 08:49:51','2022-11-16 08:49:51','테스트를 해보고 있워요 당코치님 짱 밋업 신기해용 ','2022-11-17 05:30:00','N','2022-11-17 05:00:00','당코치님의 시간을 훔쳐가요 - 괴도 경원','dgb7ehc4nfroxe4cpab1ycamay'),('Schedule',39,'2022-11-16 13:20:46','2022-11-16 13:20:46',NULL,'2022-11-17 16:00:00','N','2022-11-17 15:30:00','ㅎㅇ','pz5cc5e6pbdijpx5kexegsbbpy'),('Meeting',40,'2022-11-16 23:18:40','2022-11-16 23:18:40','비공개','2022-11-17 02:00:00','N','2022-11-17 01:30:00','1','hwbneo7anifntpqzkydnmd1inw'),('Meeting',43,'2022-11-17 04:54:18','2022-11-17 04:54:18','.','2022-11-17 07:00:00','N','2022-11-17 06:30:00','2반 1팀 피드백 신청','zs1fwzpqkb8cfgfxbxynxfqygy'),('Schedule',45,'2022-11-17 05:37:25','2022-11-17 05:37:25',NULL,'2022-11-17 09:30:00','Y','2022-11-17 09:00:00','퇴근','xfrkwcz95jg9umntccgo15zadc'),('Schedule',46,'2022-11-17 05:37:51','2022-11-17 05:38:03',NULL,'2022-11-18 01:00:00','Y','2022-11-18 00:00:00','출근','xfrkwcz95jg9umntccgo15zadc'),('Meeting',47,'2022-11-17 05:48:57','2022-11-17 05:48:57','자율 팀 회식','2022-11-17 11:00:00','N','2022-11-17 09:00:00','회식','m9ycjwgt8fgk9cb58u7mpetbdr'),('Schedule',48,'2022-11-17 05:50:06','2022-11-17 05:50:06',NULL,'2022-11-17 13:00:00','N','2022-11-17 12:00:00','PT','m9ycjwgt8fgk9cb58u7mpetbdr'),('Meeting',49,'2022-11-17 05:57:29','2022-11-17 05:57:29','꿀벌윤하','2022-11-17 09:00:00','N','2022-11-17 06:00:00','잠자는 박윤하','yrxcpqnt7jykxma7bjsdgrfcoo'),('Meeting',51,'2022-11-17 06:02:27','2022-11-17 06:02:27','바나프레소 커피','2022-11-17 21:00:00','Y','2022-11-17 20:30:00','출근','ifqf5oyyrff4dx7dbnn7r751cc'),('Meeting',53,'2022-11-17 06:04:45','2022-11-17 06:04:45','ㅎㅇ','2022-11-17 20:30:00','N','2022-11-17 20:00:00','ㅎㅇ','ifqf5oyyrff4dx7dbnn7r751cc'),('Schedule',54,'2022-11-17 06:06:14','2022-11-17 06:06:14',NULL,'2022-11-17 22:00:00','Y','2022-11-17 21:30:00','기상','jx5oz84p1i8wt84yu6zrmwfbfe'),('Schedule',57,'2022-11-17 06:11:54','2022-11-17 06:12:07',NULL,'2022-11-17 11:00:00','N','2022-11-17 09:00:00','같이 잠자는 노수빈','kwp48ombojrgtyah1tbpzgde8y'),('Meeting',61,'2022-11-17 06:29:38','2022-11-17 06:29:38','','2022-11-18 07:30:00','N','2022-11-18 07:00:00','발표 피드백','t9qcdsg1nj8h5ki4ppz7nf3i7w'),('Meeting',63,'2022-11-17 06:31:25','2022-11-17 06:31:25','피드백을 도와주세요','2022-11-17 08:30:00','N','2022-11-17 08:00:00','서울 5반','y1761jumetnqpehnc411ao5u7y'),('Schedule',64,'2022-11-17 06:33:52','2022-11-17 06:33:52',NULL,'2022-11-18 08:30:00','Y','2022-11-18 08:00:00','502팀 팀미팅','y1761jumetnqpehnc411ao5u7y'),('Schedule',66,'2022-11-17 06:36:11','2022-11-17 06:36:11',NULL,'2022-11-19 02:00:00','Y','2022-11-19 01:00:00','서울 실습코치 회의 ','d6jtmszy8fy7pbjyesipr6buky'),('Schedule',67,'2022-11-17 06:48:43','2022-11-17 06:48:43',NULL,'2022-11-17 09:00:00','N','2022-11-17 08:30:00','커피 사주기','bwtzdmegqtygi8spnaw6jk4f4c'),('Meeting',68,'2022-11-17 06:50:58','2022-11-17 06:50:58','규민누나가 사주는 커피 마시기','2022-11-18 09:00:00','N','2022-11-18 08:00:00','티타임','bwtzdmegqtygi8spnaw6jk4f4c'),('Meeting',69,'2022-11-17 06:53:19','2022-11-17 06:53:19','돼지고기 먹자','2022-11-18 09:30:00','N','2022-11-18 09:00:00','저녁먹기','bwtzdmegqtygi8spnaw6jk4f4c'),('Schedule',71,'2022-11-17 07:03:16','2022-11-17 07:03:16',NULL,'2022-11-17 10:30:00','N','2022-11-17 10:00:00','ㅁㄴㅇㅁㄴㅇ','yrxcpqnt7jykxma7bjsdgrfcoo'),('Meeting',72,'2022-11-17 07:03:43','2022-11-17 07:03:43','ㅁㅁㅁ','2022-11-18 11:30:00','N','2022-11-18 11:00:00','ㅁㅁㅁ','yrxcpqnt7jykxma7bjsdgrfcoo'),('Meeting',73,'2022-11-19 05:10:39','2022-11-19 05:10:39','누가 대신 만들어줬으면','2022-11-22 02:30:00','Y','2022-11-21 23:00:00','포토폴리오 주간 시작!','pfnfdm4febgd5qmzemdu91ri6w'),('Meeting',74,'2022-11-19 05:12:11','2022-11-19 05:12:11','다들 고생하셨습니다!','2022-11-22 08:30:00','Y','2022-11-22 06:00:00','자율 프로젝트 종료','pfnfdm4febgd5qmzemdu91ri6w'),('Meeting',75,'2022-11-19 05:12:55','2022-11-19 05:12:55','하나요?','2022-11-22 11:00:00','N','2022-11-22 10:30:00','회식','pfnfdm4febgd5qmzemdu91ri6w'),('Meeting',76,'2022-11-19 05:15:19','2022-11-19 05:15:19','온라인 언제까지 하나요?','2022-11-25 03:30:00','Y','2022-11-25 01:00:00','포토폴리오주간 종료!','pfnfdm4febgd5qmzemdu91ri6w'),('Meeting',77,'2022-11-19 05:16:12','2022-11-19 05:16:12','상담신청','2022-11-24 03:00:00','N','2022-11-24 01:30:00','포토폴리오 상담미팅 신청','pfnfdm4febgd5qmzemdu91ri6w'),('Meeting',78,'2022-11-19 05:17:19','2022-11-19 05:17:47','회고합시다','2022-11-23 08:00:00','Y','2022-11-23 04:30:00','A102 프로젝트 회고내용 상담 신청','pfnfdm4febgd5qmzemdu91ri6w'),('Meeting',79,'2022-11-19 05:18:53','2022-11-19 05:18:53','','2022-11-24 08:30:00','N','2022-11-24 07:30:00','진로상담','pfnfdm4febgd5qmzemdu91ri6w'),('Meeting',80,'2022-11-19 07:47:06','2022-11-19 07:47:06','그룹 미팅','2022-11-20 19:30:00','Y','2022-11-20 19:00:00','그룹 미팅','m1ueqgdkifby7k68arnejn5ayr'),('Meeting',81,'2022-11-19 09:32:59','2022-11-19 09:32:59','밥먹자~','2022-11-26 04:30:00','Y','2022-11-26 04:00:00','토요일 미팅','m1ueqgdkifby7k68arnejn5ayr'),('Meeting',82,'2022-11-20 11:17:52','2022-11-20 11:17:52','','2022-11-27 10:30:00','N','2022-11-27 09:30:00','저녁','syngmnbiytd8ugp5834doy1gme'),('Meeting',84,'2022-11-20 15:27:47','2022-11-20 15:27:47','## :meetup: MeetUp 밋업 발표 참고 링크  [유저 시나리오(컨설턴트)](https://meetup.gitbook.io/meetup-docs/user-scenario/consultant','2023-01-17 18:00:00','Y','2023-01-17 17:30:00','?','okdad9z7tfgh5byfzri9i7eybh'),('Meeting',85,'2022-11-20 15:50:38','2022-11-20 15:50:38','빨리요','2022-11-21 01:00:00','Y','2022-11-21 00:30:00','커피챗해요','wrbeop3scbf9fx4zewgh7d7bka');
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
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
