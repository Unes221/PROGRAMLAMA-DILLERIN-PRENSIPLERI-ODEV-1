/**
*
* @author Enes IŞIK enes.isik2@ogr.sakarya.edu.tr
* @since 23.04.2023
* <p>
* Yorumlari bulup dosyalyan ve kaç yorum yakaladıgını sayan sınıf
* </p>
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class YorumBulucu {

	public static void main(String[] args) throws IOException {
		String foksiyonAdi = null;
		String sinifAdi = null;
		int tekSayisi = 0, cokSayisi = 0, docSayisi = 0;
		System.out.println("Dosya adi girin");
		Scanner Ekran = new Scanner(System.in);
		String Dosya = Ekran.nextLine();
		Ekran.close();
		String dosyaYolu = Dosya + ".java";
		String foksiyonYolu = "\\s*public\\s+(static\\s+)?([a-zA-Z0-9<>]+)\\s+([a-zA-Z0-9_]+)\\s*\\(";
		String sinifYolu = "\\s*public\\s+class\\s+([a-zA-Z0-9_]+)";
		File tekyorumdosya = new File("teksatir.txt");
		FileWriter tekYaz = new FileWriter(tekyorumdosya);
		File cokyorumdosya = new File("coksatir.txt");
		FileWriter cokYaz = new FileWriter(cokyorumdosya);
		File docyorumdosya = new File("javadoc.txt");
		FileWriter docYaz = new FileWriter(docyorumdosya);
		try (BufferedReader dosya = new BufferedReader(new FileReader(dosyaYolu))) {
			String satir;
			Pattern foksiyonBul = Pattern.compile(foksiyonYolu);
			Pattern sinifBul = Pattern.compile(sinifYolu);
			StringBuilder tek = new StringBuilder();
			StringBuilder cok = new StringBuilder();
			StringBuilder doc = new StringBuilder();
			int parantez1 = 0;
			char harf;
			Boolean cokAcik = false, docAcik = false, degisim = false;
			while ((satir = dosya.readLine()) != null) {

				for (int i = 1; i < satir.length(); i++) {
					if (satir.charAt(i - 1) == '/' && satir.charAt(i) == '/' && i + 1 != satir.length()) {
						tekSayisi++;
						for (int j = i; j < satir.length(); j++) {
							if (satir.charAt(j) != '/') {
								tek.append(satir.charAt(j));
							}
						}
						break;
					}
				}
				if (satir.length() > 2 && satir.charAt(0) == '/' && satir.charAt(1) == '*') {
					if (satir.charAt(2) == '*') {
						docAcik = true;
						docSayisi++;
					} else {
						cokAcik = true;
						cokSayisi++;
					}
				} else if (satir.length() > 1 && satir.charAt(0) == '/' && satir.charAt(1) == '*') {
					cokAcik = true;
					cokSayisi++;
				}

				for (int c = 1; c < satir.length(); c++) {
					if (satir.charAt(c - 1) == '*' && satir.charAt(c) == '/') {
						cokAcik = false;
						docAcik = false;
					}
				}
				if (cokAcik) {
					
					for (int a = 1; a < satir.length(); a++) {
						if (satir.charAt(a - 1) == '*' && satir.charAt(a) == '/') {
							break;
						}
						if (satir.charAt(a) != '/' && satir.charAt(a) != '*') {
							cok.append(satir.charAt(a));
						}
					}
				}
				if (docAcik) {
					for (int b = 1; b < satir.length(); b++) {
						if (satir.charAt(b - 1) == '*' && satir.charAt(b) == '/') {
							break;
						}
						if (satir.charAt(b) != '/' && satir.charAt(b) != '*') {

							doc.append(satir.charAt(b));
						}
					}
				}
				Matcher sinifAra = sinifBul.matcher(satir);
				if (sinifAra.find()) {
					sinifAdi = sinifAra.group(1);
					System.out.println("Sinif : " + sinifAdi);
				}
				String kurucuYolu = "^public\\s+" + sinifAdi;
				Pattern kurucuBul = Pattern.compile(kurucuYolu);
				Matcher foksiyonAra = foksiyonBul.matcher(satir);
				Matcher kurucuAra = kurucuBul.matcher(satir);

				if (kurucuAra.find()) {
					foksiyonAdi = sinifAdi;
				}
				if (foksiyonAra.find()) {
					foksiyonAdi = foksiyonAra.group(3);
				}

				for (int i = 0; i < satir.length(); i++) {
					harf = satir.charAt(i);
					if (harf == '{') {
						parantez1++;
						degisim = true;
					} else if (harf == '}') {
						parantez1--;
					}
				}

				if (degisim && parantez1 == 1 && foksiyonAdi != null) {
					degisim = false;
					System.out.println("Fonksiyon: " + foksiyonAdi);
					System.out.println("Tek Satir Yorum Sayisi: " + tekSayisi);
					System.out.println("Cok Satir Yorum Sayisi: " + cokSayisi);
					System.out.println("Javadoc Satir Yorum Sayisi: " + docSayisi);
					System.out.println("--------------------------------");
					tekSayisi = cokSayisi = docSayisi = 0;
					tekYaz.write("Fonksiyon: " + foksiyonAdi+"\n");
					tekYaz.write(tek.toString()+"\n");
					cokYaz.write("Fonksiyon: " + foksiyonAdi+"\n");
					cokYaz.write(cok.toString()+"\n");
					docYaz.write("Fonksiyon: " + foksiyonAdi+"\n");
					docYaz.write(doc.toString()+"\n");
					tek.delete(0, tek.length());
					cok.delete(0, cok.length());
					doc.delete(0, doc.length());
				}

			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		tekYaz.close();
		cokYaz.close();
		docYaz.close();
	}
}
