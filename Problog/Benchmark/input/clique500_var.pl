% generate problem of size 500
reachable(X,Y) :- edge(X,Y) : 0.8.
reachable(X,Y) :- edge(X,Z), reachable(Z,Y) : 0.7.
same_clique(X,Y) :- reachable(X,Y), reachable(Y,X) : 0.5.
edge(0, 1) : 0.1.
edge(1, 2) : 0.9.
edge(2, 3) : 0.8.
edge(3, 4) : 0.7.
edge(4, 5) : 0.5.
edge(5, 6) : 0.5.
edge(6, 7) : 0.4.
edge(7, 8) : 0.3.
edge(8, 9) : 0.9.
edge(9, 10) : 0.2.
edge(10, 11) : 0.4.
edge(11, 12) : 0.4.
edge(12, 13) : 0.5.
edge(13, 14) : 0.4.
edge(14, 15) : 0.9.
edge(15, 16) : 0.3.
edge(16, 17) : 0.3.
edge(17, 18) : 0.9.
edge(18, 19) : 0.2.
edge(19, 20) : 0.1.
edge(20, 21) : 0.8.
edge(21, 22) : 0.3.
edge(22, 23) : 0.9.
edge(23, 24) : 0.2.
edge(24, 25) : 0.2.
edge(25, 26) : 0.6.
edge(26, 27) : 0.2.
edge(27, 28) : 0.5.
edge(28, 29) : 0.1.
edge(29, 30) : 0.3.
edge(30, 31) : 0.7.
edge(31, 32) : 0.6.
edge(32, 33) : 0.4.
edge(33, 34) : 0.4.
edge(34, 35) : 0.9.
edge(35, 36) : 0.2.
edge(36, 37) : 0.8.
edge(37, 38) : 0.8.
edge(38, 39) : 0.4.
edge(39, 40) : 0.8.
edge(40, 41) : 0.8.
edge(41, 42) : 0.1.
edge(42, 43) : 0.3.
edge(43, 44) : 0.1.
edge(44, 45) : 0.8.
edge(45, 46) : 0.7.
edge(46, 47) : 0.5.
edge(47, 48) : 0.9.
edge(48, 49) : 0.7.
edge(49, 50) : 0.6.
edge(50, 51) : 0.5.
edge(51, 52) : 0.5.
edge(52, 53) : 0.3.
edge(53, 54) : 0.2.
edge(54, 55) : 0.7.
edge(55, 56) : 0.4.
edge(56, 57) : 0.9.
edge(57, 58) : 0.2.
edge(58, 59) : 0.6.
edge(59, 60) : 0.6.
edge(60, 61) : 0.8.
edge(61, 62) : 0.2.
edge(62, 63) : 0.9.
edge(63, 64) : 0.9.
edge(64, 65) : 0.2.
edge(65, 66) : 0.3.
edge(66, 67) : 0.6.
edge(67, 68) : 0.3.
edge(68, 69) : 0.5.
edge(69, 70) : 0.5.
edge(70, 71) : 0.8.
edge(71, 72) : 0.3.
edge(72, 73) : 0.9.
edge(73, 74) : 0.4.
edge(74, 75) : 0.9.
edge(75, 76) : 0.4.
edge(76, 77) : 0.4.
edge(77, 78) : 0.2.
edge(78, 79) : 0.9.
edge(79, 80) : 0.8.
edge(80, 81) : 0.3.
edge(81, 82) : 0.8.
edge(82, 83) : 0.8.
edge(83, 84) : 0.7.
edge(84, 85) : 0.6.
edge(85, 86) : 0.9.
edge(86, 87) : 0.6.
edge(87, 88) : 0.2.
edge(88, 89) : 0.8.
edge(89, 90) : 0.3.
edge(90, 91) : 0.7.
edge(91, 92) : 0.6.
edge(92, 93) : 0.2.
edge(93, 94) : 0.5.
edge(94, 95) : 0.3.
edge(95, 96) : 0.2.
edge(96, 97) : 0.1.
edge(97, 98) : 0.6.
edge(98, 99) : 0.5.
edge(99, 100) : 0.2.
edge(100, 101) : 0.2.
edge(101, 102) : 0.6.
edge(102, 103) : 0.3.
edge(103, 104) : 0.7.
edge(104, 105) : 0.2.
edge(105, 106) : 0.3.
edge(106, 107) : 0.8.
edge(107, 108) : 0.6.
edge(108, 109) : 0.6.
edge(109, 110) : 0.6.
edge(110, 111) : 0.8.
edge(111, 112) : 0.9.
edge(112, 113) : 0.5.
edge(113, 114) : 0.9.
edge(114, 115) : 0.4.
edge(115, 116) : 0.7.
edge(116, 117) : 0.4.
edge(117, 118) : 0.7.
edge(118, 119) : 0.4.
edge(119, 120) : 0.4.
edge(120, 121) : 0.6.
edge(121, 122) : 0.6.
edge(122, 123) : 0.1.
edge(123, 124) : 0.5.
edge(124, 125) : 0.5.
edge(125, 126) : 0.2.
edge(126, 127) : 0.2.
edge(127, 128) : 0.4.
edge(128, 129) : 0.2.
edge(129, 130) : 0.1.
edge(130, 131) : 0.2.
edge(131, 132) : 0.1.
edge(132, 133) : 0.4.
edge(133, 134) : 0.4.
edge(134, 135) : 0.7.
edge(135, 136) : 0.7.
edge(136, 137) : 0.2.
edge(137, 138) : 0.8.
edge(138, 139) : 0.5.
edge(139, 140) : 0.1.
edge(140, 141) : 0.2.
edge(141, 142) : 0.7.
edge(142, 143) : 0.4.
edge(143, 144) : 0.2.
edge(144, 145) : 0.2.
edge(145, 146) : 0.4.
edge(146, 147) : 0.9.
edge(147, 148) : 0.3.
edge(148, 149) : 0.6.
edge(149, 150) : 0.1.
edge(150, 151) : 0.2.
edge(151, 152) : 0.7.
edge(152, 153) : 0.2.
edge(153, 154) : 0.3.
edge(154, 155) : 0.1.
edge(155, 156) : 0.5.
edge(156, 157) : 0.9.
edge(157, 158) : 0.8.
edge(158, 159) : 0.4.
edge(159, 160) : 0.3.
edge(160, 161) : 0.2.
edge(161, 162) : 0.1.
edge(162, 163) : 0.6.
edge(163, 164) : 0.3.
edge(164, 165) : 0.7.
edge(165, 166) : 0.5.
edge(166, 167) : 0.9.
edge(167, 168) : 0.5.
edge(168, 169) : 0.7.
edge(169, 170) : 0.3.
edge(170, 171) : 0.9.
edge(171, 172) : 0.7.
edge(172, 173) : 0.7.
edge(173, 174) : 0.8.
edge(174, 175) : 0.6.
edge(175, 176) : 0.7.
edge(176, 177) : 0.3.
edge(177, 178) : 0.8.
edge(178, 179) : 0.7.
edge(179, 180) : 0.3.
edge(180, 181) : 0.8.
edge(181, 182) : 0.8.
edge(182, 183) : 0.8.
edge(183, 184) : 0.3.
edge(184, 185) : 0.9.
edge(185, 186) : 0.5.
edge(186, 187) : 0.3.
edge(187, 188) : 0.9.
edge(188, 189) : 0.9.
edge(189, 190) : 0.6.
edge(190, 191) : 0.7.
edge(191, 192) : 0.7.
edge(192, 193) : 0.9.
edge(193, 194) : 0.9.
edge(194, 195) : 0.5.
edge(195, 196) : 0.2.
edge(196, 197) : 0.7.
edge(197, 198) : 0.7.
edge(198, 199) : 0.8.
edge(199, 200) : 0.2.
edge(200, 201) : 0.4.
edge(201, 202) : 0.8.
edge(202, 203) : 0.6.
edge(203, 204) : 0.7.
edge(204, 205) : 0.7.
edge(205, 206) : 0.8.
edge(206, 207) : 0.1.
edge(207, 208) : 0.5.
edge(208, 209) : 0.5.
edge(209, 210) : 0.8.
edge(210, 211) : 0.7.
edge(211, 212) : 0.5.
edge(212, 213) : 0.3.
edge(213, 214) : 0.6.
edge(214, 215) : 0.5.
edge(215, 216) : 0.4.
edge(216, 217) : 0.7.
edge(217, 218) : 0.9.
edge(218, 219) : 0.3.
edge(219, 220) : 0.5.
edge(220, 221) : 0.4.
edge(221, 222) : 0.8.
edge(222, 223) : 0.7.
edge(223, 224) : 0.8.
edge(224, 225) : 0.4.
edge(225, 226) : 0.3.
edge(226, 227) : 0.4.
edge(227, 228) : 0.7.
edge(228, 229) : 0.2.
edge(229, 230) : 0.3.
edge(230, 231) : 0.1.
edge(231, 232) : 0.9.
edge(232, 233) : 0.1.
edge(233, 234) : 0.5.
edge(234, 235) : 0.3.
edge(235, 236) : 0.5.
edge(236, 237) : 0.9.
edge(237, 238) : 0.5.
edge(238, 239) : 0.2.
edge(239, 240) : 0.8.
edge(240, 241) : 0.1.
edge(241, 242) : 0.6.
edge(242, 243) : 0.5.
edge(243, 244) : 0.2.
edge(244, 245) : 0.4.
edge(245, 246) : 0.4.
edge(246, 247) : 0.3.
edge(247, 248) : 0.6.
edge(248, 249) : 0.4.
edge(249, 250) : 0.4.
edge(250, 251) : 0.7.
edge(251, 252) : 0.8.
edge(252, 253) : 0.7.
edge(253, 254) : 0.3.
edge(254, 255) : 0.5.
edge(255, 256) : 0.8.
edge(256, 257) : 0.8.
edge(257, 258) : 0.4.
edge(258, 259) : 0.6.
edge(259, 260) : 0.6.
edge(260, 261) : 0.2.
edge(261, 262) : 0.3.
edge(262, 263) : 0.4.
edge(263, 264) : 0.7.
edge(264, 265) : 0.1.
edge(265, 266) : 0.4.
edge(266, 267) : 0.7.
edge(267, 268) : 0.5.
edge(268, 269) : 0.7.
edge(269, 270) : 0.1.
edge(270, 271) : 0.2.
edge(271, 272) : 0.9.
edge(272, 273) : 0.9.
edge(273, 274) : 0.6.
edge(274, 275) : 0.4.
edge(275, 276) : 0.8.
edge(276, 277) : 0.8.
edge(277, 278) : 0.9.
edge(278, 279) : 0.8.
edge(279, 280) : 0.5.
edge(280, 281) : 0.5.
edge(281, 282) : 0.8.
edge(282, 283) : 0.4.
edge(283, 284) : 0.4.
edge(284, 285) : 0.9.
edge(285, 286) : 0.1.
edge(286, 287) : 0.4.
edge(287, 288) : 0.8.
edge(288, 289) : 0.7.
edge(289, 290) : 0.3.
edge(290, 291) : 0.8.
edge(291, 292) : 0.8.
edge(292, 293) : 0.6.
edge(293, 294) : 0.1.
edge(294, 295) : 0.4.
edge(295, 296) : 0.7.
edge(296, 297) : 0.4.
edge(297, 298) : 0.8.
edge(298, 299) : 0.9.
edge(299, 300) : 0.4.
edge(300, 301) : 0.9.
edge(301, 302) : 0.6.
edge(302, 303) : 0.9.
edge(303, 304) : 0.7.
edge(304, 305) : 0.6.
edge(305, 306) : 0.4.
edge(306, 307) : 0.9.
edge(307, 308) : 0.4.
edge(308, 309) : 0.1.
edge(309, 310) : 0.1.
edge(310, 311) : 0.9.
edge(311, 312) : 0.7.
edge(312, 313) : 0.8.
edge(313, 314) : 0.3.
edge(314, 315) : 0.9.
edge(315, 316) : 0.1.
edge(316, 317) : 0.2.
edge(317, 318) : 0.5.
edge(318, 319) : 0.3.
edge(319, 320) : 0.7.
edge(320, 321) : 0.3.
edge(321, 322) : 0.9.
edge(322, 323) : 0.5.
edge(323, 324) : 0.1.
edge(324, 325) : 0.6.
edge(325, 326) : 0.7.
edge(326, 327) : 0.8.
edge(327, 328) : 0.8.
edge(328, 329) : 0.6.
edge(329, 330) : 0.8.
edge(330, 331) : 0.7.
edge(331, 332) : 0.6.
edge(332, 333) : 0.1.
edge(333, 334) : 0.8.
edge(334, 335) : 0.2.
edge(335, 336) : 0.3.
edge(336, 337) : 0.7.
edge(337, 338) : 0.3.
edge(338, 339) : 0.4.
edge(339, 340) : 0.4.
edge(340, 341) : 0.2.
edge(341, 342) : 0.1.
edge(342, 343) : 0.5.
edge(343, 344) : 0.5.
edge(344, 345) : 0.7.
edge(345, 346) : 0.6.
edge(346, 347) : 0.3.
edge(347, 348) : 0.4.
edge(348, 349) : 0.6.
edge(349, 350) : 0.9.
edge(350, 351) : 0.4.
edge(351, 352) : 0.8.
edge(352, 353) : 0.6.
edge(353, 354) : 0.9.
edge(354, 355) : 0.3.
edge(355, 356) : 0.9.
edge(356, 357) : 0.7.
edge(357, 358) : 0.8.
edge(358, 359) : 0.1.
edge(359, 360) : 0.8.
edge(360, 361) : 0.2.
edge(361, 362) : 0.1.
edge(362, 363) : 0.8.
edge(363, 364) : 0.2.
edge(364, 365) : 0.5.
edge(365, 366) : 0.6.
edge(366, 367) : 0.1.
edge(367, 368) : 0.4.
edge(368, 369) : 0.8.
edge(369, 370) : 0.8.
edge(370, 371) : 0.7.
edge(371, 372) : 0.9.
edge(372, 373) : 0.9.
edge(373, 374) : 0.2.
edge(374, 375) : 0.2.
edge(375, 376) : 0.8.
edge(376, 377) : 0.7.
edge(377, 378) : 0.8.
edge(378, 379) : 0.7.
edge(379, 380) : 0.8.
edge(380, 381) : 0.3.
edge(381, 382) : 0.5.
edge(382, 383) : 0.4.
edge(383, 384) : 0.1.
edge(384, 385) : 0.1.
edge(385, 386) : 0.2.
edge(386, 387) : 0.7.
edge(387, 388) : 0.1.
edge(388, 389) : 0.6.
edge(389, 390) : 0.9.
edge(390, 391) : 0.7.
edge(391, 392) : 0.5.
edge(392, 393) : 0.7.
edge(393, 394) : 0.7.
edge(394, 395) : 0.4.
edge(395, 396) : 0.8.
edge(396, 397) : 0.4.
edge(397, 398) : 0.8.
edge(398, 399) : 0.9.
edge(399, 400) : 0.2.
edge(400, 401) : 0.4.
edge(401, 402) : 0.3.
edge(402, 403) : 0.2.
edge(403, 404) : 0.6.
edge(404, 405) : 0.5.
edge(405, 406) : 0.9.
edge(406, 407) : 0.1.
edge(407, 408) : 0.7.
edge(408, 409) : 0.6.
edge(409, 410) : 0.9.
edge(410, 411) : 0.6.
edge(411, 412) : 0.9.
edge(412, 413) : 0.5.
edge(413, 414) : 0.5.
edge(414, 415) : 0.8.
edge(415, 416) : 0.8.
edge(416, 417) : 0.6.
edge(417, 418) : 0.8.
edge(418, 419) : 0.3.
edge(419, 420) : 0.1.
edge(420, 421) : 0.9.
edge(421, 422) : 0.6.
edge(422, 423) : 0.6.
edge(423, 424) : 0.6.
edge(424, 425) : 0.5.
edge(425, 426) : 0.3.
edge(426, 427) : 0.2.
edge(427, 428) : 0.1.
edge(428, 429) : 0.9.
edge(429, 430) : 0.3.
edge(430, 431) : 0.3.
edge(431, 432) : 0.6.
edge(432, 433) : 0.7.
edge(433, 434) : 0.7.
edge(434, 435) : 0.3.
edge(435, 436) : 0.4.
edge(436, 437) : 0.3.
edge(437, 438) : 0.5.
edge(438, 439) : 0.2.
edge(439, 440) : 0.1.
edge(440, 441) : 0.7.
edge(441, 442) : 0.6.
edge(442, 443) : 0.4.
edge(443, 444) : 0.8.
edge(444, 445) : 0.5.
edge(445, 446) : 0.7.
edge(446, 447) : 0.2.
edge(447, 448) : 0.1.
edge(448, 449) : 0.8.
edge(449, 450) : 0.5.
edge(450, 451) : 0.9.
edge(451, 452) : 0.6.
edge(452, 453) : 0.8.
edge(453, 454) : 0.3.
edge(454, 455) : 0.4.
edge(455, 456) : 0.6.
edge(456, 457) : 0.7.
edge(457, 458) : 0.7.
edge(458, 459) : 0.5.
edge(459, 460) : 0.2.
edge(460, 461) : 0.5.
edge(461, 462) : 0.1.
edge(462, 463) : 0.8.
edge(463, 464) : 0.8.
edge(464, 465) : 0.8.
edge(465, 466) : 0.5.
edge(466, 467) : 0.5.
edge(467, 468) : 0.5.
edge(468, 469) : 0.7.
edge(469, 470) : 0.3.
edge(470, 471) : 0.7.
edge(471, 472) : 0.2.
edge(472, 473) : 0.9.
edge(473, 474) : 0.7.
edge(474, 475) : 0.6.
edge(475, 476) : 0.7.
edge(476, 477) : 0.7.
edge(477, 478) : 0.1.
edge(478, 479) : 0.6.
edge(479, 480) : 0.1.
edge(480, 481) : 0.9.
edge(481, 482) : 0.9.
edge(482, 483) : 0.7.
edge(483, 484) : 0.9.
edge(484, 485) : 0.5.
edge(485, 486) : 0.5.
edge(486, 487) : 0.8.
edge(487, 488) : 0.5.
edge(488, 489) : 0.6.
edge(489, 490) : 0.4.
edge(490, 491) : 0.3.
edge(491, 492) : 0.4.
edge(492, 493) : 0.3.
edge(493, 494) : 0.4.
edge(494, 495) : 0.4.
edge(495, 496) : 0.9.
edge(496, 497) : 0.7.
edge(497, 498) : 0.9.
edge(498, 499) : 0.9.
edge(499, 500) : 0.9.
edge(500, 0) : 0.2.
