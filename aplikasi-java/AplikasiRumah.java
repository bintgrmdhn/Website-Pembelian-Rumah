/*
 * Aplikasi Pembelian Rumah
 * Copyright (c) 2026 Bintang Rhamadan (NPM 202443501817)
 *                    dan Yusuf Wibisono (NPM 202443501888).
 * Hak cipta dilindungi.
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Jendela utama aplikasi Pembelian Rumah (Swing).
 *
 * Cara menjalankan di BlueJ:
 *   klik kanan kelas AplikasiRumah > void main(String[] args) > OK.
 *
 * Fitur: daftar rumah, cari/filter, detail rumah, simulasi KPR,
 * beli rumah (tunai atau KPR) dengan struk, dan riwayat pembelian.
 */
public class AplikasiRumah extends JFrame {

    private static final String SEMUA_TIPE = "Semua tipe";

    // Pilihan rentang harga: batas bawah termasuk, batas atas tidak termasuk.
    private static final String[] LABEL_HARGA = {
        "Semua harga",
        "Di bawah Rp 500 juta",
        "Rp 500 juta - 1 miliar",
        "Rp 1 - 2 miliar",
        "Di atas Rp 2 miliar"
    };
    private static final long[] HARGA_MIN = {0L, 0L, 500_000_000L, 1_000_000_000L, 2_000_000_000L};
    private static final long[] HARGA_MAX = {Long.MAX_VALUE, 500_000_000L, 1_000_000_000L, 2_000_000_000L, Long.MAX_VALUE};

    private static final String[] KOLOM = {
        "ID", "Nama", "Tipe", "Lokasi", "LT (m\u00b2)", "LB (m\u00b2)", "KT", "KM", "Harga", "Status"
    };

    private static final Color MERAH = new Color(0xB00020);
    private static final Color HIJAU = new Color(0x1B7F3B);

    private final DataRumah data = new DataRumah();
    private List<Rumah> tampil = new ArrayList<Rumah>();   // rumah yang sedang tampil di tabel

    // Filter
    private JTextField tfCari;
    private JComboBox<String> cbTipe;
    private JComboBox<String> cbHarga;
    private JCheckBox chkTersedia;
    private JButton btnReset;

    // Tabel
    private JTable tabel;
    private DefaultTableModel model;

    // Detail rumah
    private JLabel lblNamaRumah;
    private JLabel[] nilaiDetail;

    // Simulasi KPR
    private JComboBox<Integer> cbDp;
    private JComboBox<Integer> cbTenor;
    private JTextField tfBunga;
    private JLabel lblDp;
    private JLabel lblPokok;
    private JLabel lblCicilan;
    private JLabel lblTotal;
    private JLabel lblKprInfo;

    // Bagian bawah
    private JButton btnBeli;
    private JButton btnRiwayat;
    private JLabel lblStatusBar;

    public AplikasiRumah() {
        super("Pembelian Rumah");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel isi = new JPanel(new BorderLayout(12, 12));
        isi.setBorder(new EmptyBorder(14, 14, 14, 14));
        setContentPane(isi);

        isi.add(buatPanelAtas(), BorderLayout.NORTH);
        isi.add(buatTabel(), BorderLayout.CENTER);
        isi.add(buatPanelSamping(), BorderLayout.EAST);
        isi.add(buatPanelBawah(), BorderLayout.SOUTH);

        pasangListener();
        terapkanFilter();
        if (tabel.getRowCount() > 0) {
            tabel.setRowSelectionInterval(0, 0);
        }

        setMinimumSize(new Dimension(1000, 560));
        setSize(1200, 710);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ------------------------------------------------------------------
    // Membangun tampilan
    // ------------------------------------------------------------------

    private JPanel buatPanelAtas() {
        JPanel atas = new JPanel(new BorderLayout(0, 8));

        JLabel judul = new JLabel("Pembelian Rumah");
        judul.setFont(judul.getFont().deriveFont(Font.BOLD, 26f));
        JLabel sub = new JLabel("Pilih rumah di tabel, lihat detail dan simulasi KPR, lalu beli. (Data rumah hanya contoh)");
        sub.setForeground(Color.GRAY);
        JPanel teks = new JPanel(new GridLayout(0, 1));
        teks.add(judul);
        teks.add(sub);
        atas.add(teks, BorderLayout.NORTH);

        tfCari = new JTextField(16);
        cbTipe = new JComboBox<String>();
        cbTipe.addItem(SEMUA_TIPE);
        for (String t : data.getDaftarTipe()) {
            cbTipe.addItem(t);
        }
        cbHarga = new JComboBox<String>(LABEL_HARGA);
        chkTersedia = new JCheckBox("Hanya yang tersedia");
        btnReset = new JButton("Reset");

        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        filter.add(new JLabel("Cari nama/lokasi:"));
        filter.add(tfCari);
        filter.add(new JLabel("Tipe:"));
        filter.add(cbTipe);
        filter.add(new JLabel("Harga:"));
        filter.add(cbHarga);
        filter.add(chkTersedia);
        filter.add(btnReset);
        atas.add(filter, BorderLayout.CENTER);
        return atas;
    }

    private JScrollPane buatTabel() {
        model = new DefaultTableModel(KOLOM, 0) {
            @Override
            public boolean isCellEditable(int baris, int kolom) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int kolom) {
                switch (kolom) {
                    case 0: case 4: case 5: case 6: case 7:
                        return Integer.class;
                    case 8:
                        return Long.class;
                    default:
                        return String.class;
                }
            }
        };
        tabel = new JTable(model);
        tabel.setRowHeight(26);
        tabel.setAutoCreateRowSorter(true);   // klik judul kolom untuk mengurutkan
        tabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabel.setFillsViewportHeight(true);
        tabel.getTableHeader().setReorderingAllowed(false);

        int[] lebar = {40, 190, 70, 140, 70, 70, 40, 40, 140, 80};
        for (int i = 0; i < lebar.length; i++) {
            tabel.getColumnModel().getColumn(i).setPreferredWidth(lebar[i]);
        }

        // Kolom harga: tampil sebagai Rupiah, rata kanan
        tabel.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.RIGHT);
            }

            @Override
            protected void setValue(Object nilai) {
                if (nilai instanceof Long) {
                    setText(FormatRupiah.rp(((Long) nilai).longValue()));
                } else {
                    setText(String.valueOf(nilai));
                }
            }
        });

        // Kolom status: hijau untuk tersedia, merah untuk terjual
        tabel.getColumnModel().getColumn(9).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object nilai, boolean dipilih,
                                                           boolean fokus, int baris, int kolom) {
                Component c = super.getTableCellRendererComponent(t, nilai, dipilih, fokus, baris, kolom);
                if (!dipilih) {
                    c.setForeground("Terjual".equals(nilai) ? MERAH : HIJAU);
                }
                return c;
            }
        });

        return new JScrollPane(tabel);
    }

    private JPanel buatPanelSamping() {
        // --- Detail rumah ---
        JPanel detail = new JPanel(new BorderLayout(0, 8));
        detail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Detail rumah"), new EmptyBorder(6, 8, 8, 8)));
        lblNamaRumah = new JLabel("Pilih rumah di tabel");
        lblNamaRumah.setFont(lblNamaRumah.getFont().deriveFont(Font.BOLD, 16f));
        detail.add(lblNamaRumah, BorderLayout.NORTH);

        String[] label = {"Tipe", "Lokasi", "Luas tanah", "Luas bangunan", "Kamar tidur", "Kamar mandi", "Harga", "Status"};
        JPanel grid = new JPanel(new GridLayout(0, 2, 8, 4));
        nilaiDetail = new JLabel[label.length];
        for (int i = 0; i < label.length; i++) {
            grid.add(new JLabel(label[i]));
            nilaiDetail[i] = new JLabel("-");
            grid.add(nilaiDetail[i]);
        }
        detail.add(grid, BorderLayout.CENTER);

        // --- Simulasi KPR ---
        cbDp = new JComboBox<Integer>(new Integer[]{10, 15, 20, 25, 30, 40, 50});
        cbDp.setSelectedItem(Integer.valueOf(20));
        cbTenor = new JComboBox<Integer>(new Integer[]{5, 10, 15, 20, 25, 30});
        cbTenor.setSelectedItem(Integer.valueOf(15));
        tfBunga = new JTextField("7.5");

        JPanel input = new JPanel(new GridLayout(0, 2, 8, 6));
        input.add(new JLabel("Uang muka / DP (%)"));
        input.add(cbDp);
        input.add(new JLabel("Tenor (tahun)"));
        input.add(cbTenor);
        input.add(new JLabel("Bunga per tahun (%)"));
        input.add(tfBunga);

        lblDp = new JLabel("-");
        lblPokok = new JLabel("-");
        lblCicilan = new JLabel("-");
        lblCicilan.setFont(lblCicilan.getFont().deriveFont(Font.BOLD, 15f));
        lblTotal = new JLabel("-");
        JPanel hasil = new JPanel(new GridLayout(0, 2, 8, 4));
        hasil.add(new JLabel("DP"));
        hasil.add(lblDp);
        hasil.add(new JLabel("Pokok pinjaman"));
        hasil.add(lblPokok);
        hasil.add(new JLabel("Cicilan per bulan"));
        hasil.add(lblCicilan);
        hasil.add(new JLabel("Total bayar"));
        hasil.add(lblTotal);

        lblKprInfo = new JLabel(" ");
        lblKprInfo.setForeground(MERAH);

        JPanel hasilWrap = new JPanel(new BorderLayout(0, 4));
        hasilWrap.add(hasil, BorderLayout.NORTH);
        hasilWrap.add(lblKprInfo, BorderLayout.SOUTH);

        JPanel kpr = new JPanel(new BorderLayout(0, 10));
        kpr.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Simulasi KPR"), new EmptyBorder(6, 8, 8, 8)));
        kpr.add(input, BorderLayout.NORTH);
        kpr.add(hasilWrap, BorderLayout.CENTER);

        JPanel kprWrap = new JPanel(new BorderLayout());
        kprWrap.add(kpr, BorderLayout.NORTH);

        JPanel samping = new JPanel(new BorderLayout(0, 10));
        samping.setPreferredSize(new Dimension(370, 0));
        samping.add(detail, BorderLayout.NORTH);
        samping.add(kprWrap, BorderLayout.CENTER);
        return samping;
    }

    private JPanel buatPanelBawah() {
        lblStatusBar = new JLabel(" ");
        lblStatusBar.setForeground(Color.GRAY);

        btnRiwayat = new JButton("Riwayat Pembelian");
        btnBeli = new JButton("Beli Rumah");
        btnBeli.setFont(btnBeli.getFont().deriveFont(Font.BOLD));
        btnBeli.setEnabled(false);

        JPanel tombol = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        tombol.add(btnRiwayat);
        tombol.add(btnBeli);

        JLabel hakCipta = new JLabel(
                "\u00a9 2026 Bintang Rhamadan (202443501817) dan Yusuf Wibisono (202443501888). Hak cipta dilindungi.",
                SwingConstants.CENTER);
        hakCipta.setForeground(Color.GRAY);
        hakCipta.setFont(hakCipta.getFont().deriveFont(11f));
        hakCipta.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel bawah = new JPanel(new BorderLayout());
        bawah.add(lblStatusBar, BorderLayout.WEST);
        bawah.add(tombol, BorderLayout.EAST);
        bawah.add(hakCipta, BorderLayout.SOUTH);
        return bawah;
    }

    /** Menghubungkan komponen dengan aksinya. Dipanggil setelah semua komponen jadi. */
    private void pasangListener() {
        DocumentListener saatCariBerubah = saatBerubah(new Runnable() {
            public void run() {
                terapkanFilter();
            }
        });
        tfCari.getDocument().addDocumentListener(saatCariBerubah);
        cbTipe.addActionListener(e -> terapkanFilter());
        cbHarga.addActionListener(e -> terapkanFilter());
        chkTersedia.addActionListener(e -> terapkanFilter());
        btnReset.addActionListener(e -> resetFilter());

        tabel.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                perbaruiDetail();
            }
        });

        cbDp.addActionListener(e -> perbaruiKpr());
        cbTenor.addActionListener(e -> perbaruiKpr());
        tfBunga.getDocument().addDocumentListener(saatBerubah(new Runnable() {
            public void run() {
                perbaruiKpr();
            }
        }));

        btnBeli.addActionListener(e -> prosesBeli());
        btnRiwayat.addActionListener(e -> tampilkanRiwayat());
    }

    /** DocumentListener yang menjalankan satu aksi untuk semua jenis perubahan teks. */
    private static DocumentListener saatBerubah(final Runnable aksi) {
        return new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { aksi.run(); }
            public void removeUpdate(DocumentEvent e)  { aksi.run(); }
            public void changedUpdate(DocumentEvent e) { aksi.run(); }
        };
    }

    // ------------------------------------------------------------------
    // Filter dan tabel
    // ------------------------------------------------------------------

    private void resetFilter() {
        tfCari.setText("");
        cbTipe.setSelectedIndex(0);
        cbHarga.setSelectedIndex(0);
        chkTersedia.setSelected(false);
        terapkanFilter();
    }

    /** Membaca semua isian filter, lalu mengisi ulang tabel. */
    private void terapkanFilter() {
        Rumah dipilih = rumahTerpilih();

        String tipe = (String) cbTipe.getSelectedItem();
        if (SEMUA_TIPE.equals(tipe)) {
            tipe = null;
        }
        int i = cbHarga.getSelectedIndex();
        tampil = data.cari(tfCari.getText(), tipe, HARGA_MIN[i], HARGA_MAX[i], chkTersedia.isSelected());

        model.setRowCount(0);
        for (Rumah r : tampil) {
            model.addRow(new Object[]{
                Integer.valueOf(r.getId()), r.getNama(), r.getTipe(), r.getLokasi(),
                Integer.valueOf(r.getLuasTanah()), Integer.valueOf(r.getLuasBangunan()),
                Integer.valueOf(r.getKamarTidur()), Integer.valueOf(r.getKamarMandi()),
                Long.valueOf(r.getHarga()), r.getStatus()
            });
        }

        // Pertahankan pilihan lama bila rumahnya masih tampil
        if (dipilih != null) {
            int idx = tampil.indexOf(dipilih);
            if (idx >= 0) {
                int baris = tabel.convertRowIndexToView(idx);
                tabel.setRowSelectionInterval(baris, baris);
            }
        }

        lblStatusBar.setText(tampil.size() + " rumah ditampilkan dari " + data.getSemua().size());
        perbaruiDetail();
    }

    /** Rumah yang sedang dipilih di tabel, atau null bila tidak ada. */
    private Rumah rumahTerpilih() {
        int baris = tabel.getSelectedRow();
        if (baris < 0) {
            return null;
        }
        return tampil.get(tabel.convertRowIndexToModel(baris));
    }

    // ------------------------------------------------------------------
    // Detail dan KPR
    // ------------------------------------------------------------------

    private void perbaruiDetail() {
        Rumah r = rumahTerpilih();
        if (r == null) {
            lblNamaRumah.setText("Pilih rumah di tabel");
            for (JLabel l : nilaiDetail) {
                l.setText("-");
            }
            btnBeli.setEnabled(false);
        } else {
            lblNamaRumah.setText(r.getNama());
            nilaiDetail[0].setText(r.getTipe());
            nilaiDetail[1].setText(r.getLokasi());
            nilaiDetail[2].setText(r.getLuasTanah() + " m\u00b2");
            nilaiDetail[3].setText(r.getLuasBangunan() + " m\u00b2");
            nilaiDetail[4].setText(String.valueOf(r.getKamarTidur()));
            nilaiDetail[5].setText(String.valueOf(r.getKamarMandi()));
            nilaiDetail[6].setText(FormatRupiah.rp(r.getHarga()));
            nilaiDetail[7].setText(r.getStatus());
            nilaiDetail[7].setForeground(r.isTerjual() ? MERAH : HIJAU);
            btnBeli.setEnabled(!r.isTerjual());
        }
        perbaruiKpr();
    }

    /**
     * Membuat objek SimulasiKPR dari isian di layar.
     * Mengembalikan null bila tidak ada rumah terpilih atau bunga tidak valid.
     */
    private SimulasiKPR bacaKpr(Rumah r) {
        if (r == null) {
            return null;
        }
        double bunga;
        try {
            bunga = Double.parseDouble(tfBunga.getText().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return null;
        }
        if (bunga < 0 || bunga > 30) {
            return null;
        }
        int dp = ((Integer) cbDp.getSelectedItem()).intValue();
        int tenor = ((Integer) cbTenor.getSelectedItem()).intValue();
        return new SimulasiKPR(r.getHarga(), dp, tenor, bunga);
    }

    private void perbaruiKpr() {
        Rumah r = rumahTerpilih();
        SimulasiKPR k = bacaKpr(r);
        if (k == null) {
            lblDp.setText("-");
            lblPokok.setText("-");
            lblCicilan.setText("-");
            lblTotal.setText("-");
            lblKprInfo.setText(r == null ? " " : "Bunga harus berupa angka 0 sampai 30");
            return;
        }
        lblKprInfo.setText(" ");
        lblDp.setText(FormatRupiah.rp(k.getDp()));
        lblPokok.setText(FormatRupiah.rp(k.getPokok()));
        lblCicilan.setText(FormatRupiah.rp(k.getCicilanPerBulan()));
        lblTotal.setText(FormatRupiah.rp(k.getTotalBayar()));
    }

    // ------------------------------------------------------------------
    // Pembelian
    // ------------------------------------------------------------------

    private void prosesBeli() {
        Rumah r = rumahTerpilih();
        if (r == null || r.isTerjual()) {
            return;
        }

        JTextField tfNama = new JTextField(22);
        JTextField tfHp = new JTextField(22);
        JRadioButton rbTunai = new JRadioButton("Tunai", true);
        JRadioButton rbKpr = new JRadioButton("KPR (sesuai simulasi di sebelah kanan)");
        ButtonGroup grup = new ButtonGroup();
        grup.add(rbTunai);
        grup.add(rbKpr);

        JPanel form = new JPanel(new GridLayout(0, 1, 0, 6));
        form.add(new JLabel(r.getNama() + "  -  " + FormatRupiah.rp(r.getHarga())));
        form.add(new JLabel("Nama pembeli"));
        form.add(tfNama);
        form.add(new JLabel("No. HP (angka saja)"));
        form.add(tfHp);
        form.add(new JLabel("Cara pembayaran"));
        form.add(rbTunai);
        form.add(rbKpr);

        while (true) {
            int pilihan = JOptionPane.showConfirmDialog(this, form, "Beli Rumah",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (pilihan != JOptionPane.OK_OPTION) {
                return;
            }

            String nama = tfNama.getText().trim();
            String hp = tfHp.getText().trim();
            if (nama.isEmpty()) {
                pesan("Nama pembeli wajib diisi.");
                continue;
            }
            if (!hp.matches("\\+?[0-9]{8,15}")) {
                pesan("No. HP harus berisi 8 sampai 15 angka.");
                continue;
            }
            SimulasiKPR kpr = null;
            if (rbKpr.isSelected()) {
                kpr = bacaKpr(r);
                if (kpr == null) {
                    pesan("Bunga KPR belum valid. Isi dengan angka 0 sampai 30.");
                    continue;
                }
            }

            try {
                Pembelian p = data.beli(r, nama, hp, kpr);
                terapkanFilter();
                tampilkanStruk(p);
            } catch (IllegalStateException e) {
                pesan(e.getMessage());
                terapkanFilter();
            }
            return;
        }
    }

    private void tampilkanStruk(Pembelian p) {
        JTextArea area = new JTextArea(p.buatStruk());
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setEditable(false);
        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(460, 340));
        JOptionPane.showMessageDialog(this, sp, "Pembelian Berhasil", JOptionPane.PLAIN_MESSAGE);
    }

    private void tampilkanRiwayat() {
        List<Pembelian> riwayat = data.getRiwayat();
        if (riwayat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Belum ada pembelian.", "Riwayat Pembelian",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] kolom = {"No. Transaksi", "Tanggal", "Pembeli", "Rumah", "Pembayaran", "Total bayar"};
        DefaultTableModel m = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int baris, int kol) {
                return false;
            }
        };
        for (Pembelian p : riwayat) {
            m.addRow(new Object[]{
                p.getNomor(), p.getTanggalTeks(), p.getNamaPembeli(),
                p.getRumah().getNama(), p.getMetode(), FormatRupiah.rp(p.getTotalBayar())
            });
        }
        JScrollPane sp = new JScrollPane(new JTable(m));
        sp.setPreferredSize(new Dimension(720, 220));
        JOptionPane.showMessageDialog(this, sp, "Riwayat Pembelian", JOptionPane.PLAIN_MESSAGE);
    }

    private void pesan(String teks) {
        JOptionPane.showMessageDialog(this, teks, "Perhatian", JOptionPane.WARNING_MESSAGE);
    }

    // ------------------------------------------------------------------
    // Titik masuk program
    // ------------------------------------------------------------------

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // bila gagal, pakai tampilan bawaan Java
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AplikasiRumah();
            }
        });
    }
}
