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
import com.asofterspace.toolbox.io.TextFile;
import com.asofterspace.toolbox.pdf.PdfFile;
import com.asofterspace.toolbox.pdf.PdfObject;
import com.asofterspace.toolbox.utils.DateUtils;
import com.asofterspace.toolbox.utils.Record;
import com.asofterspace.toolbox.utils.StrUtils;
import com.asofterspace.toolbox.Utils;

import java.util.ArrayList;
import java.util.List;


public class Krass {

	public final static String PROGRAM_TITLE = "Krass";
	public final static String VERSION_NUMBER = "0.0.0.9(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "8. December 2018 - 26. January 2022";

	public static AsymmetricCryptographyController cryptographyController;

	public static void main(String[] args) throws Exception {

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


		SimpleFile inputFile = new SimpleFile("C:\\home\\datacomx\\Desktop\\system\\CardOrg\\World 2.stpu");
		inputFile.setISOorUTFreadAndUTFwriteEncoding(true);
		TextFile outputFile = new TextFile("C:\\home\\out.json");
		JSON output = new JSON(Record.emptyArray());

		List<String> contents = inputFile.getContents();
		String lastDateStr = null;
		int lastTimeHour = 9;
		int i = 0;

		while (true) {
			String dateStr = contents.get(i * 3);
			String locationStr = contents.get((i * 3) + 1);
			if (dateStr.equals("%")) {
				break;
			}
			i++;

			if (dateStr.equals(lastDateStr)) {
				lastTimeHour++;
			} else {
				lastTimeHour = 9;
			}

			Record cur = Record.emptyObject();
			cur.set("date", DateUtils.parseDate(dateStr));
			cur.set("time", StrUtils.leftPad0(""+lastTimeHour, 2) + ":00");
			cur.set("where", locationStr);
			output.append(cur);

			lastDateStr = dateStr;
		}

		outputFile.saveContent(StrUtils.replaceAll(output.toString(false), " 00:00:00.000", ""));

		/*
		Directory logsDir = new Directory("C:\\home\\workbench-angepasst\\logs");
		Directory outDir = new Directory("C:\\home\\workbench-angepasst\\new");
		boolean recursively = false;
		List<File> logFiles = logsDir.getAllFilesEndingWith(".json", recursively);
		int id = 6436;

		for (File logFile : logFiles) {
			JsonFile logFileJson = new JsonFile(logFile);
			try {
				Record rec = logFileJson.getAllContents();
				rec.set("id", id);
				JsonFile outFile = new JsonFile(outDir, id + ".json");
				outFile.save(rec);

				String fileName = logFileJson.getCanonicalFilename();
				if (fileName.endsWith(".json")) {
					fileName = fileName.substring(0, fileName.length() - 5);
				}
				Directory oldDir = new Directory(fileName);
				if (oldDir.exists()) {
					oldDir.copyToDisk(new Directory(outDir, ""+id));
				}

			} catch (JsonParseException e) {
				System.err.println("The log entry " + logFile.getAbsoluteFilename() + " could not be loaded!");
				e.printStackTrace(System.err);
			}
			id++;
		}
		*/



		/*

		// read in a file and convert it to a useful srt file
		SimpleFile srtInputFile = new SimpleFile("sub1.txt");
		List<String> lines = srtInputFile.getContents();
		StringBuilder output = new StringBuilder();
		String fromTo = "";
		int lineIndex = 0;
		String curLines = "";
		int num = 1;
		for (String line : lines) {
			if ("".equals(line)) {
				if (!"".equals(curLines)) {
					output.append(num + "\r\n");
					String from = fromTo.substring(0, fromTo.indexOf(" --> "));
					String to = fromTo.substring(fromTo.indexOf(" --> ") + 5);
					double fromDouble = StrUtils.strToDouble(from) + 0.75;
					double toDouble = StrUtils.strToDouble(to) + 1.5;
					output.append(secondToTimestamp(fromDouble) + " --> " + secondToTimestamp(toDouble) + "\r\n");
					output.append(curLines + "\r\n");
					num++;
				}
				lineIndex = 0;
				curLines = "";
				continue;
			}
			if (lineIndex > 0) {
				curLines += line + "\r\n";
				lineIndex++;
			}
			if (lineIndex == 0) {
				fromTo = line;
				lineIndex++;
			}
		}
		if (!"".equals(curLines)) {
		}
		TextFile outputFile = new TextFile("out1.txt");
		outputFile.saveContent(output.toString());

/*
		// read in a different kind of file and convert it to a useful srt file
		SimpleFile srtInputFile = new SimpleFile("sub2.txt");
		List<String> lines = srtInputFile.getContents();
		StringBuilder output = new StringBuilder();
		int prevSeconds = 0;
		int curSeconds = 0;
		int nextSeconds = 0;
		int lineIndex = 0;
		String curLines = "";
		String prevCurLines = "";
		int num = 1;
		for (String line : lines) {
			if ("".equals(line)) {
				if (!"".equals(prevCurLines)) {
					output.append(num + "\r\n");
					System.out.println("prevCurLines: " + prevCurLines);
					int endTime = Math.min(curSeconds + 10, nextSeconds);
					output.append(secondToTimestamp(curSeconds) + " --> " + secondToTimestamp(endTime) + "\r\n");
					output.append(prevCurLines + "\r\n");
					num++;
				}
				lineIndex = 0;
				prevSeconds = curSeconds;
				curSeconds = nextSeconds;
				prevCurLines = curLines;
				curLines = "";
				continue;
			}
			if (lineIndex > 0) {
				curLines += line + "\r\n";
				lineIndex++;
			}
			if (lineIndex == 0) {
				nextSeconds = StrUtils.strToInt(line);
				System.out.println("nextSec: " + nextSeconds);
				lineIndex++;
			}
		}
		if (!"".equals(prevCurLines)) {
			output.append(num + "\r\n");
			int endTime = Math.min(curSeconds + 10, nextSeconds);
			output.append(secondToTimestamp(curSeconds) + " --> " + secondToTimestamp(endTime) + "\r\n");
			output.append(prevCurLines + "\r\n");
			num++;
		}
		if (!"".equals(curLines)) {
			output.append(num + "\r\n");
			int endTime = nextSeconds + 10;
			output.append(secondToTimestamp(nextSeconds) + " --> " + secondToTimestamp(endTime) + "\r\n");
			output.append(curLines + "\r\n");
			num++;
		}
		TextFile outputFile = new TextFile("out2.txt");
		outputFile.saveContent(output.toString());



/*
		TextFile prcFile = new TextFile("PRC.xml");
		String prcContent = prcFile.getContent();
		List<String> procedures = StrUtils.extractAll(prcContent,
			"<attr name=\"I1_STSPRC_Procedure::procSource_p.XString::x\">", "</attr>");
		for (String proc : procedures) {
			proc = XML.unescapeXMLstr(proc);
			String procName = StrUtils.extract(proc, "\nPROCEDURE ", "\n");
			TextFile outFile = new TextFile("proc_out/" + procName + ".STL");
			outFile.saveContent(proc);
		}

/*
		cryptographyController = new AsymmetricCryptographyController();

		TextFile outFile = new TextFile("publickey.txt");
		String key = cryptographyController.getRandomPublicKey();
		outFile.saveContent(key);

		while (true) {
			TextFile inFile = new TextFile("encpassword.txt");
			if (inFile.exists()) {
				String content = inFile.getContent().trim();
				if (!"".equals(content)) {
					decryptOrThrow(inFile.getContent().trim(), key);
					inFile.delete();
				}
			}
			Utils.sleep(3000);
		}



/*
		Directory parentDir = new Directory("C:/home/send Jayem");
		List<File> files = parentDir.getAllFilesEndingWith(".stpu", true);
		for (File file : files) {
			String oldName = file.getLocalFilename();
			String newName = oldName.substring(0, oldName.length() - 5) + ".txt";
			file.rename(newName);
		}


/*
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

				/*
				Mixer.Info[] mixers = AudioSystem.getMixerInfo();
				for (Mixer.Info mixerInfo : mixers) {
					System.out.println("mixer: " + mixerInfo.getName());
					if (!mixerInfo.getName().startsWith("Lautsprecher/Kopf")) {
						continue;
					}
					Mixer mixer = AudioSystem.getMixer(mixerInfo);
					Line.Info[] lineInfos = mixer.getSourceLineInfo();
					for (Line.Info lineInfo : lineInfos) {
						System.out.println("line: " + lineInfo.toString());
						try {
							Line line = mixer.getLine(lineInfo);

							boolean opened = line.isOpen();
							if (!(line instanceof Clip)) {
								if (!opened){
									line.open();
								}
							}
							Control[] controls = line.getControls();
							for (Control contr : controls) {
								applyVolumeToControl(contr, false, position);
							}
						} catch (LineUnavailableException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException iaEx)
						{
							System.out.println("    " + iaEx);
						}
					}
				}
				*/
	}

	/*
	private void applyVolumeToControl(Control contr, boolean on, int volume) {

		System.out.println("control: " + contr);

		if (contr instanceof FloatControl) {
			if (contr.toString().startsWith("Master Gain") || contr.toString().startsWith("Volume")) {
				FloatControl volumeCtrl = (FloatControl) contr;
				float max = volumeCtrl.getMaximum();
				float min = volumeCtrl.getMinimum();
				float newVal = min + ((volume * (max - min)) / 100);
				System.out.println("Setting value to: " + newVal);
				volumeCtrl.setValue(newVal);
			}
		}

		if (contr instanceof BooleanControl) {
			BooleanControl onOffCtrl = (BooleanControl) contr;
			onOffCtrl.setValue(on);
			System.out.println("Setting value to: " + on);
		}

		if (contr instanceof CompoundControl) {
			CompoundControl volCtrl = (CompoundControl) contr;
			Control[] members = volCtrl.getMemberControls();
			for (Control member : members) {
				applyVolumeToControl(member, on, volume);
			}
		}
	}
	*/

	private static String decryptOrThrow(String message, String key) throws Exception {
		return cryptographyController.decryptText(message, key);
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

	private static String secondToTimestamp(double secondDouble) {
		int second = (int) Math.floor(secondDouble);
		String mmm = StrUtils.leftPad0("" + (((int) Math.floor(secondDouble * 1000)) % 1000), 3);
		String ss = StrUtils.leftPad0("" + (second % 60), 2);
		String mm = StrUtils.leftPad0("" + ((second / 60) % 60), 2);
		String hh = StrUtils.leftPad0("" + ((second / (60*60)) % 60), 2);
		return hh + ":" + mm + ":" + ss + "," + mmm;
	}

	private static String secondToTimestamp(int second) {
		String mmm = "000";
		String ss = StrUtils.leftPad0("" + (second % 60), 2);
		String mm = StrUtils.leftPad0("" + ((second / 60) % 60), 2);
		String hh = StrUtils.leftPad0("" + ((second / (60*60)) % 60), 2);
		return hh + ":" + mm + ":" + ss + "," + mmm;
	}

}
