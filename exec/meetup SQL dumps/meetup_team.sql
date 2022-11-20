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
-- Table structure for table `team`
--

DROP TABLE IF EXISTS `team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team` (
  `id` varchar(255) NOT NULL,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team`
--

LOCK TABLES `team` WRITE;
/*!40000 ALTER TABLE `team` DISABLE KEYS */;
INSERT INTO `team` VALUES ('1pp3t5h9xtgdi8nj1zoidanwyr','2022-11-16 07:20:43','2022-11-16 07:20:43','7기 공통 서울3반','s07p11a3','Invite'),('1qech6p1nbfu8d31ni66djz11h','2022-11-16 13:20:07','2022-11-16 13:20:07','7기 특화 광주1반','s07p21c1','Invite'),('34gp3my9ebfg9c8qspsud7641a','2022-11-16 08:38:14','2022-11-16 08:38:14','7기 서울3반','s07p01a03','Invite'),('376bpnzrstr88m7s5i6xdc5qfc','2022-11-16 08:37:39','2022-11-16 08:37:39','6기 전자 연계 프로젝트','s06-mentoring','Invite'),('3fmzak7ay3rufqoszpmcgbw7rc','2022-11-17 05:47:49','2022-11-17 05:47:49','7기 오픈소스','s07p30os','Invite'),('3yrhfi9g5jykidu1omn64qbwcw','2022-11-16 07:17:58','2022-11-16 07:17:58','7기 서울19반','s07p01a19','Invite'),('4gfpeoh7of8fdp5b943ei4t1dr','2022-11-16 07:05:21','2022-11-16 07:05:21','7기 공통 서울2반','s07p11a2','Invite'),('5k9t7oipqt8edpbsw3gfts7yrr','2022-11-16 08:41:21','2022-11-16 08:41:21','7기 특화 서울6반','s07p21a6','Invite'),('68png8ine7ds8pcyd5aed9ys6o','2022-11-16 08:37:39','2022-11-16 08:37:39','6기 자율 서울1반','s06p30a001','Invite'),('6h7gfj9rkifhina6jkuzc9if7o','2022-11-16 08:37:39','2022-11-16 08:37:39','5기 SSDC프로젝트','s05soscon','Invite'),('6irbj4dmkiyx8qom378jfbizse','2022-11-17 05:51:57','2022-11-17 05:51:57','7기 기업연계','s07p30cl','Invite'),('6pf5bsm99iyqfeh8rmtpr384so','2022-11-16 08:37:39','2022-11-16 08:37:39','6기 자율 오픈소스반','s06p31s002','Invite'),('7hn4tgo7ntd37m33kz5m7mwe7h','2022-11-17 06:45:13','2022-11-17 06:45:13','7기 자율 서울4반','s07p30a4','Invite'),('7keiiockufyazjoo3d1i33e7pc','2022-11-16 08:37:39','2022-11-16 08:37:39','6기 공통 서울6반','s06p11a6','Invite'),('7tgzxnci3idbbkxtpr48y6gcme','2022-11-16 07:39:29','2022-11-16 07:39:29','7기 자율 서울2반','s07p30a2','Invite'),('84s3g1pdttg1bjjo1s5fhrdf1c','2022-11-16 06:52:03','2022-11-16 06:52:03','7기 특화 서울1반','s07p21a1','Invite'),('869t44mjk7gw78aki8or1f87cr','2022-11-16 07:05:21','2022-11-16 07:05:21','7기 서울2반','s07p01a02','Invite'),('8oqpukt7ginwpkbdeb64jk3xuh','2022-11-17 05:51:57','2022-11-17 05:51:57','7기 서울6반','s07p01a06','Invite'),('9zptzykge7ydmm5bfx68ggahdc','2022-11-16 08:37:39','2022-11-16 08:37:39','6기 전자연계(AEye)','s06p21s004','Invite'),('aoukne3jk7gjbx9oekwfhrcx3o','2022-11-16 07:39:29','2022-11-16 07:39:29','7기 특화 서울2반','s07p21a2','Invite'),('bnku1zq8ifbytgh9b71z1df8ze','2022-11-16 08:39:58','2022-11-16 08:39:58','7기 특화 서울3반','s07p21a3','Invite'),('c1xfnufab3gjmywe4s7c4qifzr','2022-11-16 07:17:58','2022-11-16 07:17:58','7기 특화 서울7반','s07p21a7','Invite'),('dq161qowftb7iedhmfgnzmtsor','2022-11-16 08:37:39','2022-11-16 08:37:39','컨설턴트&실습코치','s02p20000','Open'),('dwihiambeidgxcn6xuz6cjnmfr','2022-11-16 08:42:04','2022-11-16 08:42:04','7기 서울7반','s07p01a07','Invite'),('e9y13aecujdkbq48n4xw5xypyr','2022-11-16 08:37:39','2022-11-16 08:37:39','6기 자율 서울4반','s06p30a004','Invite'),('eyems6xestgf7e15tiedd6uk3w','2022-11-16 07:58:27','2022-11-16 07:58:27','7기 자율 서울5반','s07p30a5','Invite'),('fbnjxuz4jjfzbmsqg367n78doa','2022-11-16 08:22:16','2022-11-16 08:22:16','7기 공통 서울4반','s07p11a4','Invite'),('ga3gkg6o3tnpfpbb5t9xot7m3w','2022-11-16 07:17:58','2022-11-16 07:17:58','7기 공통 서울7반','s07p11a7','Invite'),('gary644oefnk3rsm1bqb3zcmyo','2022-11-16 08:37:39','2022-11-16 08:37:39','6기 자율 기업연계반','s06p31s001','Invite'),('gfguhcq9t3nipjszzpzw6war4w','2022-11-17 05:57:16','2022-11-17 05:57:16','7기 서울8반','s07p01a08','Invite'),('hgpfx9pqxiyij8zt9qz9tk8yga','2022-11-16 06:52:03','2022-11-16 06:52:03','7기 자율팀빌딩(서울)','s07p02a','Invite'),('hkhoi73cn3f39c7xa86uxpabaw','2022-11-16 06:52:03','2022-11-16 06:52:03','7기 자율 서울1반','s07p30a1','Invite'),('hm3pjsh6ejda7gb3chj4yxwnne','2022-11-16 07:24:38','2022-11-16 07:24:38','7기 서울14반','s07p01a14','Invite'),('i6booc61s3ghbcmyeos3cuy1jc','2022-11-16 07:08:44','2022-11-16 07:08:44','7기 서울4반','s07p01a04','Invite'),('ih6urirgsjfap85ymqbtnh3bsc','2022-11-17 06:33:01','2022-11-17 06:33:01','7기 자율 서울7반','s07p30a7','Invite'),('jdfpb71ui3dwzr7rf7o8h4shwe','2022-11-16 13:20:07','2022-11-16 13:20:07','7기 자율 광주1반','s07p30c1','Invite'),('jm4ktbuwop8yxqwn4jyaueajhc','2022-11-16 13:20:07','2022-11-16 13:20:07','7기 특화 광주','s07p02c','Invite'),('jnai78zewj87dfjwxtj8qmuydr','2022-11-16 07:05:21','2022-11-16 07:05:21','SSAFY 교육생 기자단','ssafycial','Invite'),('jux34siyqfbammimsh5fpgdz8o','2022-11-16 08:41:21','2022-11-16 08:41:21','7기 서울17반','s07p01a17','Invite'),('k716awud3f8ffburhbfwc9k77y','2022-11-16 07:08:44','2022-11-16 07:08:44','7기 공통 서울5반','s07p11a5','Invite'),('k84scq15pbrwpe5btnsp64qfsy','2022-11-16 08:37:39','2022-11-16 08:37:39','6기 자율 서울2반','s06p30a002','Invite'),('kh84ppsu4pgi9g1u38u8gpmn3h','2022-11-16 08:39:58','2022-11-16 08:39:58','7기 서울10반','s07p01a10','Invite'),('miuzhiqstfy1jb3r34tsww3gnr','2022-11-16 06:52:03','2022-11-16 06:52:03','7기 공통 서울8반','s07p11a8','Invite'),('o4s1oygywigh7qtc8d5qr3cnnw','2022-11-16 07:24:38','2022-11-16 07:24:38','7기 발표회','s07presentation','Invite'),('ok7ndr6kx3nxfphyrwsccoh4zh','2022-11-16 13:20:07','2022-11-16 13:20:07','7기 공통 광주2반','s07p11c2','Invite'),('owrmzzeumir1mrcom8dm5dhhnr','2022-11-16 08:37:39','2022-11-16 08:37:39','7기 특화 서울4반','s07p21a4','Invite'),('r5sqga5nupfn3f5cri5yocfaoh','2022-11-17 06:09:22','2022-11-17 06:09:22','7기 특화 서울5반','s07p21a5','Invite'),('rzy34tyuqjfmfeqgncpra8bpde','2022-11-16 08:37:39','2022-11-16 08:37:39','7기 SSDC 프로젝트','s07-ssdc','Open'),('sipru1w4fpyqpjtoy3591nsajc','2022-11-16 06:52:03','2022-11-16 06:52:03','7기 공지 전용','s07public','Invite'),('soj83ps4gtbq5ff6yp879oje5r','2022-11-16 08:39:58','2022-11-16 08:39:58','7기 공통 서울6반','s07p11a6','Invite'),('u337m85a8tfyd8qwj9g64ofy3e','2022-11-16 08:37:39','2022-11-16 08:37:39','6기 전자연계(B5F1)','s06p21s003','Invite'),('u7scneeu4bn9mfx1fyyyug9p8a','2022-11-16 07:17:58','2022-11-16 07:17:58','Java반','s04p11z02','Invite'),('u8bjc9dj63bp3x4og9mcfz9uwe','2022-11-16 07:10:39','2022-11-16 07:10:39','7기 공통 서울1반','s07p11a1','Invite'),('w9118jynjpdziroj7h7ihykgda','2022-11-16 07:08:44','2022-11-16 07:08:44','모듈형 특강','s07speciallecture','Invite'),('wgm38u7fh7nhmb371g7qcmzbpr','2022-11-16 08:37:39','2022-11-16 08:37:39','6기 공지 전용','s06public','Invite'),('wkk9i5fo4i8a8bksgwbi9ihkcr','2022-11-16 06:52:03','2022-11-16 06:52:03','7기 서울11반','s07p01a11','Invite'),('xmef3ji3ntfk9gttzmo9qys3ic','2022-11-16 08:42:14','2022-11-16 08:42:14','7기 특화 모빌리티','s07mobility','Invite'),('yr735cty4jbgzecdiz7y3pi9ya','2022-11-16 08:37:39','2022-11-16 08:37:39','6기 전자연계(동구랑둥이랑)','s06p21s002','Invite'),('z4r6uy4iofy5peb4f1er1i4dee','2022-11-16 08:37:39','2022-11-16 08:37:39','6기 자율 서울3반','s06p30a003','Invite'),('zarzeux9ytnsffd6mtgsgj11xr','2022-11-17 01:51:37','2022-11-17 01:51:37','7기 특화 부울경1반','s07p21e1','Invite'),('zf9mhdxu5fywxyazguxu85n68h','2022-11-16 08:42:14','2022-11-16 08:42:14','7기 서울5반','s07p01a05','Invite'),('zn7hwh59updqjp3iepsijhe9nr','2022-11-16 07:08:44','2022-11-16 07:08:44','Python반','s04p11z01','Invite');
/*!40000 ALTER TABLE `team` ENABLE KEYS */;
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

-- Dump completed on 2022-11-21  7:45:22
