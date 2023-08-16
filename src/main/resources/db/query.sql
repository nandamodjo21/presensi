SELECT k.tanggal,p.nama_perawat,j.jenis_absen,a.waktu FROM t_absen a JOIN tb_konfigurasi k ON a.kode_konfigurasi=k.kode_konfigurasi JOIN t_perawat p ON a.kd_perawat=p.kd_perawat JOIN t_jenis_absen j ON a.kd_jenis_absen=j.kd_jenis_absen 


