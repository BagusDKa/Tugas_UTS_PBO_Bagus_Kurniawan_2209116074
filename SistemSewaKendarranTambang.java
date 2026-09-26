import java.util.ArrayList;
import java.util.Scanner;
 
abstract class KendaraanTambang {
    protected String kodeUnit;
    protected String namaUnit;
    protected double tarifHarianDasar;
 
    public KendaraanTambang(String kodeUnit, String namaUnit, double tarifHarianDasar) {
        this.kodeUnit = kodeUnit;
        this.namaUnit = namaUnit;
        this.tarifHarianDasar = tarifHarianDasar;
    }
 
    public abstract double hitungBiayaSewa(int jumlahHari);
 
    public double hitungBiayaSewa(int jumlahHari, double persenDiskon) {
        double biayaAwal = hitungBiayaSewa(jumlahHari);
        double potongan = biayaAwal * (persenDiskon / 100.0);
        return biayaAwal - potongan;
    }
 
    public void tampilkanInfo() {
        System.out.println("Kode Unit      : " + kodeUnit);
        System.out.println("Nama Unit      : " + namaUnit);
        System.out.println("Tarif Harian   : Rp" + String.format("%,.0f", tarifHarianDasar));
        System.out.println("Kategori       : " + getKategori());
    }
 
    public abstract String getKategori();
 
    public String getNamaUnit() {
        return namaUnit;
    }
 
    public String getKodeUnit() {
        return kodeUnit;
    }
}
 
class DumpTruck extends KendaraanTambang {
    private double kapasitasTon;
 
    public DumpTruck(String kodeUnit, String namaUnit, double tarifHarianDasar, double kapasitasTon) {
        super(kodeUnit, namaUnit, tarifHarianDasar);
        this.kapasitasTon = kapasitasTon;
    }
 
    @Override
    public double hitungBiayaSewa(int jumlahHari) {
        double biaya = tarifHarianDasar * jumlahHari;
        // Condition: sewa lama dapat potongan volume
        if (jumlahHari >= 30) {
            biaya *= 0.85; // diskon 15%
        } else if (jumlahHari >= 14) {
            biaya *= 0.90; // diskon 10%
        } else if (jumlahHari >= 7) {
            biaya *= 0.95; // diskon 5%
        }
        return biaya;
    }
 
    @Override
    public String getKategori() {
        return "Alat Angkut (Hauling)";
    }
 
    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.println("Kapasitas      : " + kapasitasTon + " ton");
    }
}
 
class Excavator extends KendaraanTambang {
    private double kapasitasBucket;
 
    public Excavator(String kodeUnit, String namaUnit, double tarifHarianDasar, double kapasitasBucket) {
        super(kodeUnit, namaUnit, tarifHarianDasar);
        this.kapasitasBucket = kapasitasBucket;
    }
 
    @Override
    public double hitungBiayaSewa(int jumlahHari) {
        double biaya = tarifHarianDasar * jumlahHari;
        double biayaOperator = 250000 * jumlahHari;
        if (jumlahHari >= 20) {
            biaya *= 0.88;
        } else if (jumlahHari >= 10) {
            biaya *= 0.93;
        }
        return biaya + biayaOperator;
    }
 
    @Override
    public String getKategori() {
        return "Alat Gali (Excavating)";
    }
 
    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.println("Kapasitas Bucket: " + kapasitasBucket + " m3");
    }
}
 
class Bulldozer extends KendaraanTambang {
    private String tipeBlade;
 
    public Bulldozer(String kodeUnit, String namaUnit, double tarifHarianDasar, String tipeBlade) {
        super(kodeUnit, namaUnit, tarifHarianDasar);
        this.tipeBlade = tipeBlade;
    }
 
    @Override
    public double hitungBiayaSewa(int jumlahHari) {
        double biaya = tarifHarianDasar * jumlahHari;
        if (jumlahHari >= 15) {
            biaya *= 0.92;
        }
        return biaya;
    }
 
    @Override
    public String getKategori() {
        return "Alat Perataan (Grading)";
    }
 
    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.println("Tipe Blade     : " + tipeBlade);
    }
}
 
public class SistemSewaKendaraanTambang {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<KendaraanTambang> daftarUnit = new ArrayList<>();
    static ArrayList<String> riwayatSewa = new ArrayList<>();
 
    public static void main(String[] args) {
        inisialisasiData();
 
        boolean berjalan = true;
        while (berjalan) {
            tampilkanMenu();
            System.out.print("Pilih menu (1-5): ");
            String input = sc.nextLine().trim();
            int pilihan;
 
            try {
                pilihan = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("\n[!] Input tidak valid, masukkan angka.\n");
                continue;
            }
 
            // Condition: percabangan menu
            if (pilihan == 1) {
                tampilkanDaftarUnit();
            } else if (pilihan == 2) {
                prosesSewaUnit();
            } else if (pilihan == 3) {
                tampilkanRiwayatSewa();
            } else if (pilihan == 4) {
                tambahUnitBaru();
            } else if (pilihan == 5) {
                berjalan = false;
                System.out.println("\nTerima kasih telah menggunakan sistem sewa kendaraan tambang.");
            } else {
                System.out.println("\n[!] Pilihan tidak tersedia, coba lagi.\n");
            }
        }
        sc.close();
    }
 
    static void inisialisasiData() {
        daftarUnit.add(new DumpTruck("DT-01", "Komatsu HD785", 3500000, 91));
        daftarUnit.add(new DumpTruck("DT-02", "Caterpillar 777", 3800000, 100));
        daftarUnit.add(new Excavator("EX-01", "Komatsu PC2000", 5200000, 11));
        daftarUnit.add(new Excavator("EX-02", "Caterpillar 390F", 4900000, 9.5));
        daftarUnit.add(new Bulldozer("BD-01", "Komatsu D375A", 4000000, "Semi-U Blade"));
    }
 
    static void tampilkanMenu() {
        System.out.println("=========================================================");
        System.out.println("   SISTEM PENYEWAAN KENDARAAN / ALAT BERAT TAMBANG");
        System.out.println("=========================================================");
        System.out.println("1. Lihat Daftar Unit Tersedia");
        System.out.println("2. Sewa Unit Kendaraan");
        System.out.println("3. Lihat Riwayat Penyewaan");
        System.out.println("4. Tambah Unit Baru");
        System.out.println("5. Keluar");
        System.out.println("---------------------------------------------------------");
    }
 
    static void tampilkanDaftarUnit() {
        System.out.println("\n=================== DAFTAR UNIT TERSEDIA ===================");
        for (int i = 0; i < daftarUnit.size(); i++) {
            System.out.println("\nUnit ke-" + (i + 1));
            System.out.println("-------------------------");
            daftarUnit.get(i).tampilkanInfo(); // pemanggilan polymorphic
        }
        System.out.println("\n==============================================================\n");
    }
 
    static void prosesSewaUnit() {
        if (daftarUnit.isEmpty()) {
            System.out.println("\n[!] Belum ada unit yang terdaftar.\n");
            return;
        }
 
        tampilkanDaftarUnit();
        System.out.print("Masukkan kode unit yang ingin disewa: ");
        String kode = sc.nextLine().trim();
 
        KendaraanTambang unitDipilih = null;
        for (KendaraanTambang unit : daftarUnit) {
            if (unit.getKodeUnit().equalsIgnoreCase(kode)) {
                unitDipilih = unit;
                break;
            }
        }
 
        if (unitDipilih == null) {
            System.out.println("\n[!] Kode unit tidak ditemukan.\n");
            return;
        }
 
        int jumlahHari = 0;
        boolean valid = false;
        while (!valid) {
            System.out.print("Masukkan jumlah hari sewa: ");
            String inputHari = sc.nextLine().trim();
            try {
                jumlahHari = Integer.parseInt(inputHari);
                if (jumlahHari <= 0) {
                    System.out.println("[!] Jumlah hari harus lebih dari 0.");
                } else {
                    valid = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Masukkan angka yang valid.");
            }
        }
 
        System.out.print("Apakah pelanggan member (dapat diskon tambahan 5%)? (y/t): ");
        String jawaban = sc.nextLine().trim().toLowerCase();
 
        double totalBiaya;
        if (jawaban.equals("y")) {
            totalBiaya = unitDipilih.hitungBiayaSewa(jumlahHari, 5.0); // overload dengan diskon
        } else {
            totalBiaya = unitDipilih.hitungBiayaSewa(jumlahHari); // versi override dasar
        }
 
        System.out.println("\n===================== RINCIAN SEWA =====================");
        System.out.println("Unit             : " + unitDipilih.getNamaUnit() + " (" + unitDipilih.getKategori() + ")");
        System.out.println("Jumlah Hari Sewa : " + jumlahHari + " hari");
        System.out.println("Status Member    : " + (jawaban.equals("y") ? "Ya" : "Tidak"));
        System.out.println("Total Biaya      : Rp" + String.format("%,.0f", totalBiaya));
        System.out.println("==========================================================\n");
 
        String catatan = unitDipilih.getNamaUnit() + " | " + jumlahHari + " hari | Rp"
                + String.format("%,.0f", totalBiaya);
        riwayatSewa.add(catatan);
        System.out.println("[✓] Transaksi berhasil dicatat dalam riwayat.\n");
    }
 
    static void tampilkanRiwayatSewa() {
        System.out.println("\n===================== RIWAYAT PENYEWAAN =====================");
        if (riwayatSewa.isEmpty()) {
            System.out.println("Belum ada transaksi penyewaan.");
        } else {
            for (int i = 0; i < riwayatSewa.size(); i++) {
                System.out.println((i + 1) + ". " + riwayatSewa.get(i));
            }
        }
        System.out.println("===============================================================\n");
    }
 
    static void tambahUnitBaru() {
        System.out.println("\n--- Tambah Unit Baru ---");
        System.out.println("Pilih jenis unit:");
        System.out.println("1. Dump Truck (Alat Angkut)");
        System.out.println("2. Excavator (Alat Gali)");
        System.out.println("3. Bulldozer (Alat Perataan)");
        System.out.print("Pilihan: ");
        String jenis = sc.nextLine().trim();
 
        System.out.print("Kode unit: ");
        String kode = sc.nextLine().trim();
        System.out.print("Nama unit: ");
        String nama = sc.nextLine().trim();
        System.out.print("Tarif harian dasar (Rp): ");
        double tarif;
        try {
            tarif = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[!] Tarif tidak valid, unit batal ditambahkan.\n");
            return;
        }
 
        if (jenis.equals("1")) {
            System.out.print("Kapasitas (ton): ");
            double kapasitas = parseDoubleAman(sc.nextLine().trim());
            daftarUnit.add(new DumpTruck(kode, nama, tarif, kapasitas));
            System.out.println("[✓] Dump Truck berhasil ditambahkan.\n");
        } else if (jenis.equals("2")) {
            System.out.print("Kapasitas bucket (m3): ");
            double kapasitas = parseDoubleAman(sc.nextLine().trim());
            daftarUnit.add(new Excavator(kode, nama, tarif, kapasitas));
            System.out.println("[✓] Excavator berhasil ditambahkan.\n");
        } else if (jenis.equals("3")) {
            System.out.print("Tipe blade: ");
            String blade = sc.nextLine().trim();
            daftarUnit.add(new Bulldozer(kode, nama, tarif, blade));
            System.out.println("[✓] Bulldozer berhasil ditambahkan.\n");
        } else {
            System.out.println("[!] Jenis unit tidak dikenali, unit batal ditambahkan.\n");
        }
    }
 
    static double parseDoubleAman(String teks) {
        try {
            return Double.parseDouble(teks);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
