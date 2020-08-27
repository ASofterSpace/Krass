/**
 * Unlicensed code created by A Softer Space, 2018
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.krass;

import com.asofterspace.toolbox.io.BinaryFile;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.io.JSON;
import com.asofterspace.toolbox.io.JsonFile;
import com.asofterspace.toolbox.io.JsonParseException;
import com.asofterspace.toolbox.io.SimpleFile;
import com.asofterspace.toolbox.io.XML;
import com.asofterspace.toolbox.pdf.PdfFile;
import com.asofterspace.toolbox.pdf.PdfObject;
import com.asofterspace.toolbox.utils.Record;
import com.asofterspace.toolbox.Utils;

import java.util.ArrayList;
import java.util.List;


public class Krass {

	public final static String PROGRAM_TITLE = "Krass";
	public final static String VERSION_NUMBER = "0.0.0.8(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "8. December 2018 - 27. August 2020";

	public static void main(String[] args) {

		// let the Utils know in what program it is being used
		Utils.setProgramTitle(PROGRAM_TITLE);
		Utils.setVersionNumber(VERSION_NUMBER);
		Utils.setVersionDate(VERSION_DATE);

		if (args.length > 0) {
			if (args[0].equals("--version")) {
				System.out.println(Utils.getFullProgramIdentifierWithDate());
				return;
			}

			if (args[0].equals("--version_for_zip")) {
				System.out.println("version " + Utils.getVersionNumber());
				return;
			}
		}


		SimpleFile file = new SimpleFile("C:/Users/Moyaccercchi/Downloads/2019-dec-html.html");
		List<String> lines = file.getContents();

		List<String> output1 = new ArrayList<>();
		List<String> output2 = new ArrayList<>();
		String curOutput1 = null;
		String curOutput2 = null;
		for (String line : lines) {
			if (line.startsWith("<p style") && line.contains("px;left:145px;white-space:nowrap")) {
				line = line.substring(line.indexOf(">") + 1);
				line = line.substring(0, line.indexOf("<"));
				line = XML.unescapeXMLstr(line);
				if (curOutput1 == null) {
					curOutput1 = line;
					curOutput2 = "";
				} else {
					if (curOutput1.endsWith("-")) {
						curOutput1 = curOutput1.substring(0, curOutput1.length() - 1);
					}
					curOutput1 += line;
				}
			} else if (line.startsWith("<p style") && line.contains("px;left:247px;white-space:nowrap") && (curOutput2 != null)) {
				line = line.substring(line.indexOf(">") + 1);
				line = line.substring(0, line.indexOf("<"));
				line = XML.unescapeXMLstr(line);
				if (curOutput2.endsWith("-")) {
					curOutput2 = curOutput2.substring(0, curOutput2.length() - 1);
				}
				curOutput2 += line;
			} else {
				if (curOutput1 != null) {
					output1.add(curOutput1);
					output2.add(curOutput2);
				}
				curOutput1 = null;
				curOutput2 = null;
			}
		}
		SimpleFile outputFile1 = new SimpleFile("out1.txt");
		outputFile1.saveContents(output1);
		System.out.println("Wrote output to " + outputFile1.getAbsoluteFilename());
		SimpleFile outputFile2 = new SimpleFile("out2.txt");
		outputFile2.saveContents(output2);
		System.out.println("Wrote output to " + outputFile2.getAbsoluteFilename());

		// uncompressPdf("C:/home/2019-dec.pdf", "C:/home/2019-dec-moya.pdf");

		// addDebugToJsBridge();

		/*
		searchForKeyValue();

		/*
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


		DefaultImageFile qrImgFile = new DefaultImageFile("qrpic1.png");
		Image qrImg = qrImgFile.getImage();
		QrCode qrReadOut = QrCodeFactory.readFromQrImage(qrImg);
		System.out.println("Read out qrpic1.png, containing: " + qrReadOut.getContent());

		QrCode outCode = QrCodeFactory.createFromString("https://www.asofterspace.com/");
		PpmFile outCodeFile = new PpmFile("qroutpic1.ppm");
		outCodeFile.assign(outCode.toImage());
		outCodeFile.save();
		System.out.println("Created qroutpic1.png, containing: " + outCode.getContent());


		// addDisclaimerToProject("D:/prog/asofterspace/CdmScriptEditor/src");
		*/

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
			try {
				System.out.println(obj.getPlainStreamContent());
			} catch (Exception e) {
				// whoops!
			}
		}

		pdf.save();
	}

	private static void searchForKeyValue() {

		JsonFile jsonIn = new JsonFile("in.json");
		try {
			JSON json = jsonIn.getAllContents();

			// now check for the interior element "uniqueIdentifier": "6abe85db-633c-11ea-acad-fb7c401fce62",
			// every location where this occurs, and report it!
			json.linkDoubly();
			List<Record> foundRecs = json.searchForKeyValue("uniqueIdentifier", "6abe85db-633c-11ea-acad-fb7c401fce62");
			for (Record foundRec : foundRecs) {
				System.out.println(foundRec.getPath());
			}

			JsonFile jsonOut = new JsonFile("out.json");
			jsonOut.setAllContents(json);
			jsonOut.save();
		} catch (JsonParseException e) {
			System.err.println("Could not parse in.json! " + e);
		}
	}

	private static void addDebugToJsBridge() {

		Directory restRoot = new Directory("C:\\home\\a softer space\\worky work\\egs-cc\\recoded web app\\jsbridge\\impl\\co.recoded.egscc.server\\src\\main\\java\\co\\recoded\\egscc\\rest");

		boolean recursively = true;

		List<File> restFiles = restRoot.getAllFiles(recursively);

		List<String> methods = new ArrayList<>();
		methods.add("GET");
		methods.add("POST");
		methods.add("PUT");
		methods.add("DELETE");

		for (File restFile : restFiles) {
			SimpleFile sFile = new SimpleFile(restFile);
			String content = sFile.getContent();
			int startIndex = content.indexOf("\n@Path(\"");
			if (startIndex < 0) {
				continue;
			}
			String path = content.substring(startIndex + 8);
			path = path.substring(0, path.indexOf("\""));

			String nextDebugLineStart = null;
			String nextDebugLinePath = null;
			List<String> nextDebugArguments = null;
			String lastDebugArgument = null;
			List<String> newContent = new ArrayList<>();
			for (String line : sFile.getContents()) {
				newContent.add(line);
				String trimLine = line.trim();
				if (trimLine.startsWith("package ")) {
					newContent.add("import co.recoded.egscc.utils.DebugLog;");
				}
				for (String method : methods) {
					if (trimLine.equals("@" + method)) {
						nextDebugLineStart = "DebugLog.logRestCall(\"" + method + "\", \"" + path + "\", ";
					}
				}
				if (trimLine.startsWith("@Path(\"")) {
					nextDebugLinePath = trimLine.substring(7);
					nextDebugLinePath = nextDebugLinePath.substring(0, nextDebugLinePath.indexOf("\""));
				}
				if (trimLine.startsWith("public ") && trimLine.contains("(")) {
					nextDebugArguments = new ArrayList<>();
					trimLine = trimLine.substring(trimLine.indexOf("(") + 1);
				}
				if (nextDebugArguments != null) {
					while (trimLine.contains(",")) {
						String beforeComma = trimLine.substring(0, trimLine.indexOf(","));
						beforeComma = beforeComma.trim();
						if (beforeComma.contains(" ")) {
							beforeComma = beforeComma.substring(beforeComma.lastIndexOf(" ") + 1);
						}
						nextDebugArguments.add(beforeComma);
						trimLine = trimLine.substring(trimLine.indexOf(",")+1);
					}
					if (trimLine.contains(")")) {
						String beforeComma = trimLine.substring(0, trimLine.lastIndexOf(")"));
						beforeComma = beforeComma.trim();
						if (beforeComma.contains(" ")) {
							beforeComma = beforeComma.substring(beforeComma.lastIndexOf(" ") + 1);
						}
						if (!"".equals(beforeComma)) {
							lastDebugArgument = beforeComma;
						}
					}
				}
				if (trimLine.endsWith("{")) {
					if (nextDebugLineStart != null) {
						String nextDebugLine = "        " + nextDebugLineStart;
						if (nextDebugLinePath == null) {
							nextDebugLine += "\"\"";
						} else {
							nextDebugLine += "\"" + nextDebugLinePath + "\"";
						}
						if (nextDebugArguments != null) {
							for (String arg : nextDebugArguments) {
								nextDebugLine += ", " + arg;
							}
						}
						if (lastDebugArgument != null) {
							nextDebugLine += ", " + lastDebugArgument;
						}
						nextDebugLine += ");";
						newContent.add(nextDebugLine);
					}
					nextDebugLineStart = null;
					nextDebugLinePath = null;
					nextDebugArguments = null;
					lastDebugArgument = null;
				}
			}
			sFile.saveContents(newContent);
		}

	}

}
