-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 25, 2023 at 09:25 AM
-- Server version: 10.4.17-MariaDB
-- PHP Version: 7.3.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_rentalkendaraan`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_jenis`
--

CREATE TABLE `tbl_jenis` (
  `id_jenis` char(7) NOT NULL,
  `jenis` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_jenis`
--

INSERT INTO `tbl_jenis` (`id_jenis`, `jenis`) VALUES
('jns_1', 'Mobil'),
('jns_2', 'Motor'),
('jns_3', 'Bus');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_kendaraan`
--

CREATE TABLE `tbl_kendaraan` (
  `id_kendaraan` char(7) NOT NULL,
  `id_jenis` char(7) NOT NULL,
  `id_merek` char(7) NOT NULL,
  `model` varchar(50) NOT NULL,
  `tahun_pembuatan` char(4) NOT NULL,
  `nomor_polisi` varchar(10) NOT NULL,
  `warna` varchar(50) NOT NULL,
  `status_tersedia` tinyint(4) NOT NULL DEFAULT 1,
  `harga_sewa_per_hari` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_kendaraan`
--

INSERT INTO `tbl_kendaraan` (`id_kendaraan`, `id_jenis`, `id_merek`, `model`, `tahun_pembuatan`, `nomor_polisi`, `warna`, `status_tersedia`, `harga_sewa_per_hari`) VALUES
('k_1', 'jns_1', 'mk_1', 'All New Avanza', '2022', 'BP1001FO', 'Silver', 0, 350000),
('k_2', 'jns_1', 'mk_5', 'RX-8', '2000', 'BP19X', 'Merah', 1, 750000),
('k_3', 'jns_1', 'mk_5', 'FD2R', '2002', 'BP11', 'Hitam', 1, 3500000),
('k_4', 'jns_2', 'mk_2', 'BEAT FI', '2021', 'BP3915U', 'Hitam', 1, 60000),
('k_5', 'jns_3', 'mk_8', 'OH 1626 L', '2020', 'B7611EE', 'Putih', 1, 5000000);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_merek`
--

CREATE TABLE `tbl_merek` (
  `id_merek` char(7) NOT NULL,
  `merek` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_merek`
--

INSERT INTO `tbl_merek` (`id_merek`, `merek`) VALUES
('mk_1', 'Toyota'),
('mk_2', 'Honda'),
('mk_3', 'Yamaha'),
('mk_4', 'Xenia'),
('mk_5', 'Mazda'),
('mk_6', 'Suzuki'),
('mk_7', 'Chery'),
('mk_8', 'Mercedes');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_penyewa`
--

CREATE TABLE `tbl_penyewa` (
  `id_penyewa` char(7) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `alamat` varchar(75) NOT NULL,
  `nomor_telepon` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_penyewa`
--

INSERT INTO `tbl_penyewa` (`id_penyewa`, `nama`, `alamat`, `nomor_telepon`) VALUES
('p_1', 'Adry Mirza', 'Cendana', '0819920019'),
('p_2', 'Martin', 'Kintamani', '081239910'),
('p_3', 'Intan', 'Tiban', '081912881'),
('p_4', 'Dinda', 'Tiban Koperasi', '08129991312'),
('p_5', 'Eka', 'Bengkong', '0817551618'),
('p_6', 'Dhonny', 'Sei Ladi', '08951002911');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_penyewaan`
--

CREATE TABLE `tbl_penyewaan` (
  `id_penyewaan` char(7) NOT NULL,
  `id_kendaraan` char(7) NOT NULL,
  `id_penyewa` char(7) NOT NULL,
  `tanggal_sewa` date NOT NULL,
  `tanggal_pengembalian` date NOT NULL,
  `jumlah_hari` int(11) NOT NULL,
  `total_biaya` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_penyewaan`
--

INSERT INTO `tbl_penyewaan` (`id_penyewaan`, `id_kendaraan`, `id_penyewa`, `tanggal_sewa`, `tanggal_pengembalian`, `jumlah_hari`, `total_biaya`) VALUES
('sw-1', 'k_1', 'p_1', '2023-12-25', '2023-12-31', 6, 2100000),
('sw-2', 'k_4', 'p_2', '2023-12-18', '2023-12-24', 6, 360000);

--
-- Triggers `tbl_penyewaan`
--
DELIMITER $$
CREATE TRIGGER `trigerAvailability` AFTER INSERT ON `tbl_penyewaan` FOR EACH ROW BEGIN
        UPDATE db_rentalkendaraan.`tbl_kendaraan` SET `status_tersedia` = '0' WHERE `id_kendaraan` = NEW.id_kendaraan;
    END
$$
DELIMITER ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_jenis`
--
ALTER TABLE `tbl_jenis`
  ADD PRIMARY KEY (`id_jenis`);

--
-- Indexes for table `tbl_kendaraan`
--
ALTER TABLE `tbl_kendaraan`
  ADD PRIMARY KEY (`id_kendaraan`),
  ADD KEY `fk_jenis` (`id_jenis`),
  ADD KEY `fk_merek` (`id_merek`);

--
-- Indexes for table `tbl_merek`
--
ALTER TABLE `tbl_merek`
  ADD PRIMARY KEY (`id_merek`);

--
-- Indexes for table `tbl_penyewa`
--
ALTER TABLE `tbl_penyewa`
  ADD PRIMARY KEY (`id_penyewa`);

--
-- Indexes for table `tbl_penyewaan`
--
ALTER TABLE `tbl_penyewaan`
  ADD PRIMARY KEY (`id_penyewaan`),
  ADD KEY `fk_kendaraan` (`id_kendaraan`),
  ADD KEY `fk_penyewa` (`id_penyewa`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tbl_kendaraan`
--
ALTER TABLE `tbl_kendaraan`
  ADD CONSTRAINT `fk_jenis` FOREIGN KEY (`id_jenis`) REFERENCES `tbl_jenis` (`id_jenis`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_merek` FOREIGN KEY (`id_merek`) REFERENCES `tbl_merek` (`id_merek`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tbl_penyewaan`
--
ALTER TABLE `tbl_penyewaan`
  ADD CONSTRAINT `fk_kendaraan` FOREIGN KEY (`id_kendaraan`) REFERENCES `tbl_kendaraan` (`id_kendaraan`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_penyewa` FOREIGN KEY (`id_penyewa`) REFERENCES `tbl_penyewa` (`id_penyewa`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
