/**
 * Unlicensed code created by A Softer Space, 2018
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.krass;

import com.asofterspace.toolbox.io.BinaryFile;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.io.PdfFile;
import com.asofterspace.toolbox.io.PdfObject;
import com.asofterspace.toolbox.Utils;

import java.util.List;


public class Main {

	public final static String PROGRAM_TITLE = "Krass";
	public final static String VERSION_NUMBER = "0.0.0.2(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "8. December 2018 - 18. December 2018";

	public static void main(String[] args) {

		// let the Utils know in what program it is being used
		Utils.setProgramTitle(PROGRAM_TITLE);
		Utils.setVersionNumber(VERSION_NUMBER);
		Utils.setVersionDate(VERSION_DATE);

		// addDisclaimerToProject("D:/prog/asofterspace/assAddressBook/src");

		PdfFile pdf = new PdfFile("blubb.pdf");
		pdf.create("blubb");
		pdf.save();

		exportPicsFromPdf("ex3.pdf");

		replacePicsInPdf("ex3.pdf", "ex3_out.pdf", "picex3.pdf/newpic.jpg");

		uncompressPdf("ex_compressed.pdf", "ex_uncompressed.pdf");
	}

	private static void addDisclaimerToProject(String projectPath) {

		Directory addDisclaimerDir = new Directory(projectPath);

		boolean recursively = true;

		List<File> allFiles = addDisclaimerDir.getAllFiles(recursively);

		for (File file : allFiles) {
			if (file.getFilename().endsWith(".java")) {
				String newContent =
					"/**\r\n" +
					" * Unlicensed code created by A Softer Space, 2018\r\n" +
					" * www.asofterspace.com/licenses/unlicense.txt\r\n" +
					" */\r\n" +
					file.getContent();
				file.setContent(newContent);
				file.save();
			}
		}
	}

	private static void exportPicsFromPdf(String pdfPath) {

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
						filter = "null";
					}
					System.out.println("filter: " + filter);
					switch (filter) {
						case "/DCTDecode": //JPEG
						case "/JPX": // JPEG2000
							BinaryFile jpgFile = new BinaryFile("out" + pdfPath + "/Image" + obj.getNumber() + ".jpg");
							jpgFile.saveContent(obj.getStreamContent());
							break;
						case "/FlateDecode": // PNG? or just generic, could be anything?
							BinaryFile pngFile = new BinaryFile("out" + pdfPath + "/Image" + obj.getNumber() + ".png");
							pngFile.saveContent(obj.getPlainStreamContent());
							break;
						default:
							System.out.println("The image cannot be saved as the filter is not understood! :(");
							break;
					}
				}
			}
		}
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
