/**
 * Unlicensed code created by A Softer Space, 2018
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.krass;

import com.asofterspace.toolbox.barcodes.QrCode;
import com.asofterspace.toolbox.io.BinaryFile;
import com.asofterspace.toolbox.io.DefaultImageFile;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.io.PdfFile;
import com.asofterspace.toolbox.io.PdfObject;
import com.asofterspace.toolbox.io.PpmFile;
import com.asofterspace.toolbox.io.SimpleFile;
import com.asofterspace.toolbox.utils.ColorRGB;
import com.asofterspace.toolbox.utils.Image;
import com.asofterspace.toolbox.Utils;

import java.util.List;


public class Main {

	public final static String PROGRAM_TITLE = "Krass";
	public final static String VERSION_NUMBER = "0.0.0.3(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "8. December 2018 - 27. March 2019";

	public static void main(String[] args) {

		// let the Utils know in what program it is being used
		Utils.setProgramTitle(PROGRAM_TITLE);
		Utils.setVersionNumber(VERSION_NUMBER);
		Utils.setVersionDate(VERSION_DATE);

		exportPicsFromPdf("ex.pdf");

		PpmFile ppm = new PpmFile("outex.pdf/Image4.ppm");

		// great!
		// now try to read out a version 3 QR code...
		// for now, let's just hardcode the QR code location:
		// it starts at 1175, 4
		// it is rotated left - but our fancy QrCode should automatically notice that! ^^
		// it is a bit larger than one pixel per QR-pixel, but not quite two pixels per QR-pixel
		int offsetX = 1175;
		int offsetY = 4;
		int enlargeX = 0;
		int enlargeY = 0;
		QrCode code = new QrCode(3);
		for (int x = 0; x < code.getWidth(); x++) {
			for (int y = 0; y < code.getHeight(); y++) {
				code.setDatapoint(x, y, ppm.getPixel(offsetX + x + enlargeX, offsetY + y + enlargeY).isDark());
				if (y % 4 != 3) {
					enlargeY++;
				}
			}
			if (x % 4 != 3) {
				enlargeX++;
			}
			enlargeY = 0;
		}

		System.out.println(code);


		// write an exemplary bitmap mask as ppm file
		Image img = new Image(29, 29);
		ColorRGB black = new ColorRGB(0, 0, 0);

		for (int x = 0; x < 29; x++) {
			for (int y = 0; y < 29; y++) {
				if (((x*y)%3 + x + y) % 2 == 0) {
					img.setPixel(x, y, black);
				}
			}
		}

		PpmFile outFile = new PpmFile("mask.ppm");
		outFile.assign(img);
		outFile.save();



		for (int i = 1; i < 4; i++) {
			DefaultImageFile inFile1 = new DefaultImageFile("pic" + i + ".png");
			PpmFile outFile1 = new PpmFile("pic" + i + ".ppm");
			outFile1.assign(inFile1.getImage());
			outFile1.save();
		}


		// addDisclaimerToProject("D:/prog/asofterspace/CdmScriptEditor/src");

		/*
		PdfFile pdf = new PdfFile("blubb.pdf");
		pdf.create("blubb");
		pdf.save();

		exportPicsFromPdf("ex3.pdf");

		replacePicsInPdf("ex3.pdf", "ex3_out.pdf", "picex3.pdf/newpic.jpg");

		uncompressPdf("ex_compressed.pdf", "ex_uncompressed.pdf");
		*/
	}

	private static void addDisclaimerToProject(String projectPath) {

		Directory addDisclaimerDir = new Directory(projectPath);

		boolean recursively = true;

		List<File> allFiles = addDisclaimerDir.getAllFiles(recursively);

		for (File file : allFiles) {
			if (file.getFilename().endsWith(".java")) {
				SimpleFile simpleFile = new SimpleFile(file);
				String newContent =
					"/**\r\n" +
					" * Unlicensed code created by A Softer Space, 2019\r\n" +
					" * www.asofterspace.com/licenses/unlicense.txt\r\n" +
					" */\r\n" +
					simpleFile.getContent();
				simpleFile.setContent(newContent);
				simpleFile.save();
			}
		}
	}

	private static void exportPicsFromPdf(String pdfPath) {

		PdfFile pdf = new PdfFile(pdfPath);
		Directory targetDir = new Directory("out" + pdfPath);
		pdf.exportPictures(targetDir);

		/*
		PdfFile pdf = new PdfFile(pdfPath);
		List<PdfObject> objects = pdf.getObjects();

		for (PdfObject obj : objects) {
			if ("/XObject".equals(obj.getDictValue("/Type"))) {
				if ("/Image".equals(obj.getDictValue("/Subtype"))) {
					System.out.println("");
					System.out.println("Found image " + obj.getNumber() + ":");
					System.out.println("width: " + obj.getDictValue("/Width"));
					System.out.println("height: " + obj.getDictValue("/Height"));
					String filter = obj.getDictValue("/Filter");
					if (filter == null) {
						filter = "";
					}
					System.out.println("filter: " + filter);
					switch (filter) {
						case "": // Portable Pix Map
							// TODO :: do something about the ICCBased ColorSpace? (right now we are just ignoring it...)
							BinaryFile ppmFile = new BinaryFile("out" + pdfPath + "/Image" + obj.getNumber() + ".ppm");
							String header = "P6\n" + obj.getDictValue("/Width") + " " + obj.getDictValue("/Height") + "\n255\n";
							ppmFile.saveContentStr(header + obj.getPlainStreamContent());
							break;
						case "/DCTDecode": //JPEG
						case "/JPX": // JPEG2000
							BinaryFile jpgFile = new BinaryFile("out" + pdfPath + "/Image" + obj.getNumber() + ".jpg");
							jpgFile.saveContentStr(obj.getStreamContent());
							break;
						case "/FlateDecode": // PNG? or just generic, could be anything?
							BinaryFile pngFile = new BinaryFile("out" + pdfPath + "/Image" + obj.getNumber() + ".png");
							pngFile.saveContentStr(obj.getPlainStreamContent());
							break;
						default:
							System.out.println("The image cannot be saved as the filter is not understood! :(");
							BinaryFile otherFile = new BinaryFile("out" + pdfPath + "/Image" + obj.getNumber() + ".bmp");
							otherFile.saveContentStr(obj.getPlainStreamContent());
							break;
					}
				}
			} else {
				String filter = obj.getDictValue("/Filter");
				if (filter == null) {
					filter = "null";
				}
				switch (filter) {
					case "/ASCIIHexDecode":
						BinaryFile hexFile = new BinaryFile("out" + pdfPath + "/Data" + obj.getNumber() + ".txt");
						hexFile.saveContentStr(obj.getPlainStreamContent());
						break;
				}
			}
		}
		*/
	}

	private static void replacePicsInPdf(String origPdfPath, String newPdfPath, String newPicPath) {

		File oldFile = new File(origPdfPath);
		oldFile.copyToDisk(newPdfPath);

		PdfFile pdf = new PdfFile(newPdfPath);
		List<PdfObject> objects = pdf.getObjects();

		BinaryFile newPicFile = new BinaryFile(newPicPath);
		String newPicContent = newPicFile.loadContentStr();

		for (PdfObject obj : objects) {
			if ("/XObject".equals(obj.getDictValue("/Type"))) {
				if ("/Image".equals(obj.getDictValue("/Subtype"))) {
					System.out.println("");
					System.out.println("Found image " + obj.getNumber() + ":");
					System.out.println("width: " + obj.getDictValue("/Width"));
					System.out.println("height: " + obj.getDictValue("/Height"));
					obj.setDictValue("/ColorSpace", "/DeviceRGB");
					obj.setDictValue("/BitsPerComponent", "8");
					obj.setDictValue("/Filter", "/DCTDecode");
					obj.setDictValue("/Interpolate", "true");
					obj.setDictValue("/Width", "640");
					obj.setDictValue("/Height", "853");
					obj.removeDictValue("/SMask");
					obj.removeDictValue("/Matte");
					obj.setStreamContent(newPicContent);
				}
			}
		}

		pdf.save();
	}

	private static void uncompressPdf(String origPdfPath, String newPdfPath) {

		File oldFile = new File(origPdfPath);
		oldFile.copyToDisk(newPdfPath);

		PdfFile pdf = new PdfFile(newPdfPath);
		List<PdfObject> objects = pdf.getObjects();

		for (PdfObject obj : objects) {
			obj.uncompress();
		}

		pdf.save();
	}

}
