SELECT p.nama_perawat,j.tanggal,ja.jenis_absen,a.waktu FROM t_absen a JOIN t_perawat p ON(a.kd_perawat=p.kd_perawat) JOIN t_jadwal j ON(a.kode_jadwal=j.kode_jadwal) JOIN t_jenis_absen ja ON(a.kd_jenis_absen=ja.kd_jenis_absen) WHERE kd_absen


-- query absen
SELECT p.nama, tj.tim, r.nama_ruangan, s.shift, k.tanggal FROM tb_konfigurasi k LEFT JOIN tb_tim t ON (k.kd_tim= t.kode_tim) LEFT JOIN t_jenis_tim tj ON (t.id_jenis_tim= tj.id_jenis_tim) LEFT JOIN rawat_inap r on (k.kode_ruangan= r.kode_ruangan) LEFT JOIN tb_shift s on (k.kode_shift= s.kode_shift) JOIN tb_anggota_tim ta ON (k.kd_tim= ta.kd_tim) LEFT JOIN pegawai p on (ta.nira= p.nira) WHERE ta.nira =(SELECT kode_perawat FROM t_user where id_user = :id_user) AND k.tanggal = date(now()) AND time(now()) BETWEEN s.waktu AND s.waktu_pulang