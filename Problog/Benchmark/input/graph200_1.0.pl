% generate problem of size 200
reachable(X,Y) :- edge(X,Y) : 1.0.
reachable(X,Y) :- edge(X,Z), reachable(Z,Y) : 1.0.
increasing(X,Y) :- edge(X,Y), lt(X,Y) : 1.0.
increasing(X,Y) :- edge(X,Z), lt(X,Z), increasing(Z,Y) : 1.0.
edge(0, 1) : 1.0.
edge(1, 2) : 1.0.
edge(2, 3) : 1.0.
edge(3, 4) : 1.0.
edge(4, 5) : 1.0.
edge(5, 6) : 1.0.
edge(6, 7) : 1.0.
edge(7, 8) : 1.0.
edge(8, 9) : 1.0.
edge(9, 10) : 1.0.
edge(10, 11) : 1.0.
edge(11, 12) : 1.0.
edge(12, 13) : 1.0.
edge(13, 14) : 1.0.
edge(14, 15) : 1.0.
edge(15, 16) : 1.0.
edge(16, 17) : 1.0.
edge(17, 18) : 1.0.
edge(18, 19) : 1.0.
edge(19, 20) : 1.0.
edge(20, 21) : 1.0.
edge(21, 22) : 1.0.
edge(22, 23) : 1.0.
edge(23, 24) : 1.0.
edge(24, 25) : 1.0.
edge(25, 26) : 1.0.
edge(26, 27) : 1.0.
edge(27, 28) : 1.0.
edge(28, 29) : 1.0.
edge(29, 30) : 1.0.
edge(30, 31) : 1.0.
edge(31, 32) : 1.0.
edge(32, 33) : 1.0.
edge(33, 34) : 1.0.
edge(34, 35) : 1.0.
edge(35, 36) : 1.0.
edge(36, 37) : 1.0.
edge(37, 38) : 1.0.
edge(38, 39) : 1.0.
edge(39, 40) : 1.0.
edge(40, 41) : 1.0.
edge(41, 42) : 1.0.
edge(42, 43) : 1.0.
edge(43, 44) : 1.0.
edge(44, 45) : 1.0.
edge(45, 46) : 1.0.
edge(46, 47) : 1.0.
edge(47, 48) : 1.0.
edge(48, 49) : 1.0.
edge(49, 50) : 1.0.
edge(50, 51) : 1.0.
edge(51, 52) : 1.0.
edge(52, 53) : 1.0.
edge(53, 54) : 1.0.
edge(54, 55) : 1.0.
edge(55, 56) : 1.0.
edge(56, 57) : 1.0.
edge(57, 58) : 1.0.
edge(58, 59) : 1.0.
edge(59, 60) : 1.0.
edge(60, 61) : 1.0.
edge(61, 62) : 1.0.
edge(62, 63) : 1.0.
edge(63, 64) : 1.0.
edge(64, 65) : 1.0.
edge(65, 66) : 1.0.
edge(66, 67) : 1.0.
edge(67, 68) : 1.0.
edge(68, 69) : 1.0.
edge(69, 70) : 1.0.
edge(70, 71) : 1.0.
edge(71, 72) : 1.0.
edge(72, 73) : 1.0.
edge(73, 74) : 1.0.
edge(74, 75) : 1.0.
edge(75, 76) : 1.0.
edge(76, 77) : 1.0.
edge(77, 78) : 1.0.
edge(78, 79) : 1.0.
edge(79, 80) : 1.0.
edge(80, 81) : 1.0.
edge(81, 82) : 1.0.
edge(82, 83) : 1.0.
edge(83, 84) : 1.0.
edge(84, 85) : 1.0.
edge(85, 86) : 1.0.
edge(86, 87) : 1.0.
edge(87, 88) : 1.0.
edge(88, 89) : 1.0.
edge(89, 90) : 1.0.
edge(90, 91) : 1.0.
edge(91, 92) : 1.0.
edge(92, 93) : 1.0.
edge(93, 94) : 1.0.
edge(94, 95) : 1.0.
edge(95, 96) : 1.0.
edge(96, 97) : 1.0.
edge(97, 98) : 1.0.
edge(98, 99) : 1.0.
edge(99, 100) : 1.0.
edge(100, 101) : 1.0.
edge(101, 102) : 1.0.
edge(102, 103) : 1.0.
edge(103, 104) : 1.0.
edge(104, 105) : 1.0.
edge(105, 106) : 1.0.
edge(106, 107) : 1.0.
edge(107, 108) : 1.0.
edge(108, 109) : 1.0.
edge(109, 110) : 1.0.
edge(110, 111) : 1.0.
edge(111, 112) : 1.0.
edge(112, 113) : 1.0.
edge(113, 114) : 1.0.
edge(114, 115) : 1.0.
edge(115, 116) : 1.0.
edge(116, 117) : 1.0.
edge(117, 118) : 1.0.
edge(118, 119) : 1.0.
edge(119, 120) : 1.0.
edge(120, 121) : 1.0.
edge(121, 122) : 1.0.
edge(122, 123) : 1.0.
edge(123, 124) : 1.0.
edge(124, 125) : 1.0.
edge(125, 126) : 1.0.
edge(126, 127) : 1.0.
edge(127, 128) : 1.0.
edge(128, 129) : 1.0.
edge(129, 130) : 1.0.
edge(130, 131) : 1.0.
edge(131, 132) : 1.0.
edge(132, 133) : 1.0.
edge(133, 134) : 1.0.
edge(134, 135) : 1.0.
edge(135, 136) : 1.0.
edge(136, 137) : 1.0.
edge(137, 138) : 1.0.
edge(138, 139) : 1.0.
edge(139, 140) : 1.0.
edge(140, 141) : 1.0.
edge(141, 142) : 1.0.
edge(142, 143) : 1.0.
edge(143, 144) : 1.0.
edge(144, 145) : 1.0.
edge(145, 146) : 1.0.
edge(146, 147) : 1.0.
edge(147, 148) : 1.0.
edge(148, 149) : 1.0.
edge(149, 150) : 1.0.
edge(150, 151) : 1.0.
edge(151, 152) : 1.0.
edge(152, 153) : 1.0.
edge(153, 154) : 1.0.
edge(154, 155) : 1.0.
edge(155, 156) : 1.0.
edge(156, 157) : 1.0.
edge(157, 158) : 1.0.
edge(158, 159) : 1.0.
edge(159, 160) : 1.0.
edge(160, 161) : 1.0.
edge(161, 162) : 1.0.
edge(162, 163) : 1.0.
edge(163, 164) : 1.0.
edge(164, 165) : 1.0.
edge(165, 166) : 1.0.
edge(166, 167) : 1.0.
edge(167, 168) : 1.0.
edge(168, 169) : 1.0.
edge(169, 170) : 1.0.
edge(170, 171) : 1.0.
edge(171, 172) : 1.0.
edge(172, 173) : 1.0.
edge(173, 174) : 1.0.
edge(174, 175) : 1.0.
edge(175, 176) : 1.0.
edge(176, 177) : 1.0.
edge(177, 178) : 1.0.
edge(178, 179) : 1.0.
edge(179, 180) : 1.0.
edge(180, 181) : 1.0.
edge(181, 182) : 1.0.
edge(182, 183) : 1.0.
edge(183, 184) : 1.0.
edge(184, 185) : 1.0.
edge(185, 186) : 1.0.
edge(186, 187) : 1.0.
edge(187, 188) : 1.0.
edge(188, 189) : 1.0.
edge(189, 190) : 1.0.
edge(190, 191) : 1.0.
edge(191, 192) : 1.0.
edge(192, 193) : 1.0.
edge(193, 194) : 1.0.
edge(194, 195) : 1.0.
edge(195, 196) : 1.0.
edge(196, 197) : 1.0.
edge(197, 198) : 1.0.
edge(198, 199) : 1.0.
edge(199, 200) : 1.0.
edge(200, 0) : 1.0.