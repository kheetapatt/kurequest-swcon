package ku.cs.services;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.InputStream;

public class PDFMaker {



    public void makePDF(String date,
                        String name,
                        String year,
                        String id,
                        String phone,
                        String mail,
                        String faculty,
                        String major,
                        String advisor,
                        String academic_year,
                        String semester,
                        String from,
                        String to,
                        String reason,
                        String requestFor,
                        String type) {


        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            InputStream fontStream = PDFMaker.class.getResourceAsStream("/font/THSarabunNew.ttf");
            InputStream boldFontStream = PDFMaker.class.getResourceAsStream("/font/THSarabunNew Bold.ttf");
            PDType0Font thaiFont = PDType0Font.load(document, fontStream);
            PDType0Font thaiFontBold = PDType0Font.load(document, boldFontStream);

            InputStream imageStream = PDFMaker.class.getResourceAsStream("/images/logo-ku-request.png");
            PDImageXObject logo = PDImageXObject.createFromByteArray(document, imageStream.readAllBytes(), "logo");
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.drawImage(logo, 30, 680, 100, 100);

            String headdate = date.split(" ")[0];
            contentStream.beginText();
            contentStream.setFont(thaiFontBold, 30);
            contentStream.newLineAtOffset(140, 740);
            contentStream.showText("มหาวิทยาลัยเกษตรศาสตร์");
            contentStream.endText();

            if(type.equals("คำร้องทั่วไป")) {
                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 20);
                contentStream.newLineAtOffset(140, 710);
                contentStream.showText("คำร้องทั่วไป / General Request");
                contentStream.endText();
            }else if(type.equals("คำร้องขอลงทะเบียนเรียน")) {
                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 20);
                contentStream.newLineAtOffset(140, 710);
                contentStream.showText("คำร้องขอลงทะเบียนเรียน/ Request for Registration");
                contentStream.endText();
            } else if (type.equals("ขอลาออก")) {


                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 20);
                contentStream.newLineAtOffset(140, 710);
                contentStream.showText("ใบขอลาออก/ Resignation Form");
                contentStream.endText();
            }
            contentStream.beginText();
            contentStream.setFont(thaiFont, 15);
            contentStream.newLineAtOffset(510, 750);
            contentStream.showText("สำนักบริการศึกษา");
            contentStream.endText();

            contentStream.addRect(490, 700, 110, 75);
            contentStream.stroke();

            contentStream.beginText();
            contentStream.setFont(thaiFont, 15);
            contentStream.newLineAtOffset(500, 730);
            contentStream.showText("เลขที่..............................");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFont, 15);
            contentStream.newLineAtOffset(500, 710);
            contentStream.showText("วันที่..............................");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFont, 15);
            contentStream.newLineAtOffset(500, 680);
            contentStream.showText("วันที่  ");
            contentStream.showText(headdate);
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFont, 16);
            contentStream.newLineAtOffset(50, 660);
            contentStream.showText("เรียน ........................................................");
            contentStream.endText();


            contentStream.beginText();
            contentStream.setFont(thaiFont, 16);
            contentStream.newLineAtOffset(100, 665);
            contentStream.showText(advisor);
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFont, 16);
            contentStream.newLineAtOffset(50, 640);
            contentStream.showText("To        (อาจารย์ที่ปรึกษา/Advisor)");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFontBold, 18);
            contentStream.newLineAtOffset(80, 620);
            contentStream.showText("ชื่อนิสิต (นาย/นาง/นางสาว)");
            contentStream.setFont(thaiFont, 18);
            contentStream.showText("...............................................................................................................");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFont, 18);
            contentStream.newLineAtOffset(80, 605);
            contentStream.showText("Student’s name (Mr./Mrs./Miss)");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFont, 18);
            contentStream.newLineAtOffset(220, 625);
            contentStream.showText(name);
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFontBold, 18);
            contentStream.newLineAtOffset(550, 620);
            contentStream.showText("ตัวบรรจง");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFont, 18);
            contentStream.newLineAtOffset(540, 605);
            contentStream.showText("(Print name)");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFontBold, 18);
            contentStream.newLineAtOffset(50, 580);
            contentStream.showText("เลขประจำตัวนิสิต");
            contentStream.setFont(thaiFont, 18);
            contentStream.showText("  ................................");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFont, 18);
            contentStream.newLineAtOffset(50, 565);
            contentStream.showText("Studet ID number");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFont, 18);
            contentStream.newLineAtOffset(155, 585);
            contentStream.showText(id);
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFontBold, 18);
            contentStream.newLineAtOffset(250, 580);
            contentStream.showText("นิสิตชั้นปีที่");
            contentStream.setFont(thaiFont, 18);
            contentStream.showText("  ......................");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFont, 18);
            contentStream.newLineAtOffset(240, 565);
            contentStream.showText("Academic Level");
            contentStream.endText();


            contentStream.beginText();
            contentStream.setFont(thaiFont, 18);
            contentStream.newLineAtOffset(330, 585);
            contentStream.showText(year);
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFontBold, 18);
            contentStream.newLineAtOffset(380, 580);
            contentStream.showText("คณะ");
            contentStream.setFont(thaiFont, 18);
            contentStream.showText("  ...............................................................");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFont, 18);
            contentStream.newLineAtOffset(375, 565);
            contentStream.showText("Faculty");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFont, 18);
            contentStream.newLineAtOffset(420, 585);
            contentStream.showText(faculty);
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFontBold, 18);
            contentStream.newLineAtOffset(50, 545);
            contentStream.showText("สาขา");
            contentStream.setFont(thaiFont, 18);
            contentStream.showText("  .............................................");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFont, 18);
            contentStream.newLineAtOffset(50, 530);
            contentStream.showText("Major Field");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFont, 18);
            contentStream.newLineAtOffset(100, 550);
            contentStream.showText(major);
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFontBold, 18);
            contentStream.newLineAtOffset(210, 545);
            contentStream.showText("หมายเลขโทรศัพท์");
            contentStream.setFont(thaiFont, 18);
            contentStream.showText("  .....................................");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFont, 18);
            contentStream.newLineAtOffset(210, 530);
            contentStream.showText("Phone number");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(thaiFont, 18);
            contentStream.newLineAtOffset(310, 550);
            contentStream.showText(phone);
            contentStream.endText();


            if(type.equals("ขอลาออก")) {
                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 20);
                contentStream.newLineAtOffset(140, 710);
                contentStream.showText("ใบขอลาออก/ Resignation Form");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 18);
                contentStream.newLineAtOffset(50, 505);
                contentStream.showText("มีความประสงค์ขอลาออก เนื่องจาก");
                contentStream.setFont(thaiFont, 18);
                contentStream.showText("  .................................................................................................................................................");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(50, 490);
                contentStream.showText("Reason(s) for Resignation ");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(230, 510);
                contentStream.showText(reason);
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 18);
                contentStream.newLineAtOffset(50, 465);
                contentStream.showText("จึงขอลาออกตั้งแต่บัดนี้เป็นต้นไป และข้าพเจ้าไม่มีหนี้สินค้างชำระ");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(50, 450);
                contentStream.showText("This resignation is effective immediately. I have no outstanding debt");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 18);
                contentStream.newLineAtOffset(290, 430);
                contentStream.showText("ลงนามนิสิต/ผู้ดำเนินการแทน");
                contentStream.setFont(thaiFont,18);
                contentStream.showText("  ................................................................");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(280, 415);
                contentStream.showText("Student/Person Requesting Signature");
                contentStream.endText();

                contentStream.addRect(45, 100, 280, 150);
                contentStream.addRect(325, 250, 280, 150);
                contentStream.addRect(45, 250, 280, 150);
                contentStream.stroke();

                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 18);
                contentStream.newLineAtOffset(60, 380);
                contentStream.showText("เรียน หัวหน้าภาควิชา");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(60, 365);
                contentStream.showText("To Head of department");
                contentStream.endText();

                contentStream.addRect(64, 350, 10, 10);
                contentStream.stroke();
                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(70, 350);
                contentStream.showText("  เห็นชอบ  Approved");
                contentStream.endText();

                contentStream.addRect(64, 335, 10, 10);
                contentStream.stroke();
                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(70, 335);
                contentStream.showText("  ไม่เห็นชอบ  Denied");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(80, 320);
                contentStream.showText("ลงนาม/Signature  ....................................................");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(170, 305);
                contentStream.showText("(............................................ )");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(200, 290);
                contentStream.showText("..../...../........");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(180, 270);
                contentStream.showText("อาจารย์ที่ปรึกษา Advisor");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 18);
                contentStream.newLineAtOffset(330, 380);
                contentStream.showText("เรียน คณบดี");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(330, 365);
                contentStream.showText("To Dean");
                contentStream.endText();

                contentStream.addRect(334, 350, 10, 10);
                contentStream.stroke();
                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(340, 350);
                contentStream.showText("  เห็นชอบ  Approved");
                contentStream.endText();

                contentStream.addRect(334, 335, 10, 10);
                contentStream.stroke();
                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(340, 335);
                contentStream.showText("  ไม่เห็นชอบ  Denied");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(350, 320);
                contentStream.showText("ลงนาม/Signature  ....................................................");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(460, 305);
                contentStream.showText("(............................................ )");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(490, 290);
                contentStream.showText("..../...../........");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(420, 270);
                contentStream.showText(" หัวหน้าภาควิชา Head of Department");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 18);
                contentStream.newLineAtOffset(60, 230);
                contentStream.showText("คำพิจารณาคณบดีเจ้าสังกัด");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(60, 215);
                contentStream.showText("Dean’s decision");
                contentStream.endText();

                contentStream.addRect(64, 200, 10, 10);
                contentStream.stroke();
                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(70, 200);
                contentStream.showText("  อนุมัติ  Approved");
                contentStream.endText();

                contentStream.addRect(64, 185, 10, 10);
                contentStream.stroke();
                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(70, 185);
                contentStream.showText("  ไม่อนุมัติ  Denied");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(80, 170);
                contentStream.showText("ลงนาม/Signature  ....................................................");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(170, 155);
                contentStream.showText("(............................................ )");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(200, 140);
                contentStream.showText("..../...../........");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(200, 125);
                contentStream.showText("คณบดี Dean");
                contentStream.endText();

            }else if (type.equals("คำร้องขอลงทะเบียนเรียน"))
            {

                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 18);
                contentStream.newLineAtOffset(400, 545);
                contentStream.showText("E-mail");
                contentStream.setFont(thaiFont, 18);
                contentStream.showText("  .........................................................");
                contentStream.endText();
                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(450, 550);
                contentStream.showText(mail);
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 18);
                contentStream.newLineAtOffset(50, 505);
                contentStream.showText("มีความประสงค์");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(50, 490);
                contentStream.showText("Request for");
                contentStream.endText();
                if (requestFor.equals("ลงทะเบียนล่าช้าหรือรักษาสถานภาพนิสิตล่าช้า")) {

                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 18);
                contentStream.newLineAtOffset(90, 475);
                contentStream.showText(" ลงทะเบียนเรียนล่าช้าหรือรักษาสถานภาพนิสิตล่าช้า (แนบ KU 1)");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(95, 460);
                contentStream.showText("Late Registration or Late Maintain student status (Attach KU 1)");
                contentStream.endText();

                } else if (requestFor.equals("เพิ่มหรือถอนรายวิชาล่าช้า")) {


                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 18);
                contentStream.newLineAtOffset(90, 465);
                contentStream.showText(" เพิ่มหรือถอนรายวิชาล่าช้า (แนบ KU 3) ");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(95, 450);
                contentStream.showText("Late Add or Drop (Attach KU 3) ");
                contentStream.endText();

                } else if (requestFor.equals("ลงทะเบียนเกิน 22 หน่วยกิตสำหรับต้นหรือปลาย หรือลงทะเบียนเรียนเกิน 7 หน่วยสำหรับภาคฤดูร้อน"))
                {
                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 16);
                contentStream.newLineAtOffset(90, 475);
                contentStream.showText("ลงทะเบียนเกิน 22 หน่วยกิตสำหรับภาคต้นหรือปลาย หรือลงทะเบียนเกิน 7 หน่วยกิตสำหรับภาคฤดูร้อน (แนบ KU 3) ");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 14);
                contentStream.newLineAtOffset(95, 460);
                contentStream.showText("Registration over 22 credits for a regular semester or 7 credits for Summer Session (Attach KU 3) ");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(100, 445);
                contentStream.showText("ภาค.......................");
                contentStream.showText("ปีการศึกษา....................");
                contentStream.showText("เปลี่ยนจาก...........................................");
                contentStream.showText("หน่วยกิต เป็น...............................");
                contentStream.showText("หน่วยกิต");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(95, 448);
                contentStream.showText("         " + semester);
                contentStream.showText("                            " + academic_year);
                contentStream.showText("                          " + from);
                contentStream.showText("                                           " + to);
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(105, 430);
                contentStream.showText(" Semester               Academic Year               From credits                credits To");
                contentStream.endText();
            }else if(requestFor.equals("ลงทะเบียนต่ำกว่า 9 หน่วยกิต")) {
                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 18);
                contentStream.newLineAtOffset(90, 475);
                contentStream.showText("ลงทะเบียนต่ำกว่า 9 หน่วยกิต");
                contentStream.setFont(thaiFont, 14);
                contentStream.showText("    Registration less than 9 credits");
                contentStream.endText();
            }else if(requestFor.equals("ผ่อนผันค่าธรรมเนียมการศึกษา")){
                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 18);
                contentStream.newLineAtOffset(90, 475);
                contentStream.showText("ผ่อนผันค่าธรรมเนียมการศึกษา");
                contentStream.setFont(thaiFont, 14);
                contentStream.showText("    Postpone tuition and fee payments");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(95, 460);
                contentStream.showText("ภาค.....................................");
                contentStream.showText("ปีการศึกษา..........................................");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(95, 445);
                contentStream.showText("Semester");
                contentStream.showText("                    Academic Year");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(95, 463);
                contentStream.showText("        " + semester);
                contentStream.showText("                                        " + academic_year);
                contentStream.endText();
                } else if (requestFor.equals("ย้ายคณะ หรือเปลี่ยนสาขาวิชาเอก")) {
                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 18);
                contentStream.newLineAtOffset(90, 475);
                contentStream.showText("ย้ายคณะ หรือเปลี่ยนสาขาวิชาเอก");
                contentStream.setFont(thaiFont, 14);
                contentStream.showText("    Transferring to another Faculty or Changing the major subject");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(95, 460);
                contentStream.showText("จาก.....................................");
                contentStream.showText("เป็น..........................................");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(95, 445);
                contentStream.showText("From");
                contentStream.showText("                          To");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(95, 463);
                contentStream.showText("       " + from);
                contentStream.showText("                             " + to);
                contentStream.endText();
            }if(!reason.isEmpty()) {
                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 18);
                contentStream.newLineAtOffset(100, 413);
                contentStream.showText("เนื่องจาก");
                contentStream.setFont(thaiFont, 14);
                contentStream.showText(" Reason(s) for the request");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(105, 394);
                contentStream.showText(".....................................................................................................................................................");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(120, 397);
                contentStream.showText(reason);
                contentStream.endText();
                }
                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 18);
                contentStream.newLineAtOffset(290, 380);
                contentStream.showText("ลงนามนิสิต/ผู้ดำเนินการแทน");
                contentStream.setFont(thaiFont, 18);
                contentStream.showText("  ................................................................");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(280, 365);
                contentStream.showText("Student/Person Requesting Signature");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 14);
                contentStream.newLineAtOffset(480, 365);
                contentStream.showText("  วันที่ " + headdate);
                contentStream.endText();


                contentStream.addRect(55, 50, 260, 150);
                contentStream.addRect(315, 200, 260, 150);
                contentStream.addRect(55, 200, 260, 150);

                contentStream.stroke();

                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 16);
                contentStream.newLineAtOffset(60, 335);
                contentStream.showText("เรียน หัวหน้าภาควิชา");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(60, 320);
                contentStream.showText("To Head of department");
                contentStream.endText();

                contentStream.addRect(64, 305, 10, 10);
                contentStream.stroke();
                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(70, 305);
                contentStream.showText("  เห็นชอบ  Approved");
                contentStream.endText();

                contentStream.addRect(64, 290, 10, 10);
                contentStream.stroke();
                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(70, 290);
                contentStream.showText("  ไม่เห็นชอบ  Denied");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(80, 275);
                contentStream.showText("ลงนาม/Signature  ....................................................");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(170, 260);
                contentStream.showText("(............................................ )");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(200, 245);
                contentStream.showText("....../......./..........");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(180, 230);
                contentStream.showText("อาจารย์ที่ปรึกษา Advisor");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 16);
                contentStream.newLineAtOffset(330, 335);
                contentStream.showText("เรียน คณบดี");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(330, 320);
                contentStream.showText("To Dean");
                contentStream.endText();

                contentStream.addRect(338, 305, 10, 10);
                contentStream.stroke();
                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(345, 305);
                contentStream.showText("  เห็นชอบ  Approved");
                contentStream.endText();

                contentStream.addRect(338, 290, 10, 10);
                contentStream.stroke();
                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(345, 290);
                contentStream.showText("  ไม่เห็นชอบ  Denied");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(355, 275);
                contentStream.showText("ลงนาม/Signature  ....................................................");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(445, 260);
                contentStream.showText("(............................................ )");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(475, 245);
                contentStream.showText("....../......./..........");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(400, 230);
                contentStream.showText("หัวหน้าภาควิชา Head of department");
                contentStream.endText();
                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 16);
                contentStream.newLineAtOffset(60, 180);
                contentStream.showText("คำพิจารณาคณบดี");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(60, 165);
                contentStream.showText("Dean’s decision");
                contentStream.endText();

                contentStream.addRect(64, 150, 10, 10);
                contentStream.stroke();
                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(70, 150);
                contentStream.showText("  อนุมัติ  Approved");
                contentStream.endText();

                contentStream.addRect(64, 135, 10, 10);
                contentStream.stroke();
                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(70, 135);
                contentStream.showText("  ไม่อนุมัติ  Denied");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(80, 120);
                contentStream.showText("ลงนาม/Signature  ....................................................");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(150, 105);
                contentStream.showText("(............................................ )");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(180, 95);
                contentStream.showText("....../......./..........");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(175, 80);
                contentStream.showText("คณบดี Dean");
                contentStream.endText();

            } else if (type.equals("คำร้องทั่วไป")) {
                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 18);
                contentStream.newLineAtOffset(50, 505);
                contentStream.showText("มีความประสงค์");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(50, 490);
                contentStream.showText("Request for");
                contentStream.endText();
                if (requestFor.equals("ใบแทนปริญญาบัตรชำรุด") || requestFor.equals("ใบแทนปริญญาบัตรสูญหาย")) {
                    contentStream.beginText();
                    contentStream.setFont(thaiFontBold, 18);
                    contentStream.newLineAtOffset(90, 475);
                    contentStream.showText(" ใบแทนปริญญาบัตร");
                    contentStream.setFont(thaiFont, 18);
                    contentStream.showText(" Replacement of Certificate");
                    contentStream.endText();
                    if (requestFor.equals("ใบแทนปริญญาบัตรชำรุด")) {
                        contentStream.beginText();
                        contentStream.setFont(thaiFontBold, 18);
                        contentStream.newLineAtOffset(115, 460);
                        contentStream.showText("ชำรุด (แนบปริญญาบัตรที่ชำรุดและสำเนาบัตรประชาชน)");
                        contentStream.endText();

                        contentStream.beginText();
                        contentStream.setFont(thaiFont, 18);
                        contentStream.newLineAtOffset(115, 445);
                        contentStream.showText("Damaged (Attach Damaged Certificate and a copy of ID card)");
                        contentStream.endText();
                    } else {
                        contentStream.beginText();
                        contentStream.setFont(thaiFontBold, 18);
                        contentStream.newLineAtOffset(115, 430);
                        contentStream.showText("สูญหาย (แนบใบแจ้งความและสำเนาบัตรประชาชน)");
                        contentStream.endText();

                        contentStream.beginText();
                        contentStream.setFont(thaiFont, 18);
                        contentStream.newLineAtOffset(115, 415);
                        contentStream.showText("Lost (Attach Royal Thai Police Report and a copy of ID card)");
                        contentStream.endText();
                    }
                    contentStream.beginText();
                    contentStream.setFont(thaiFontBold, 18);
                    contentStream.newLineAtOffset(90, 400);
                    contentStream.showText("กรณีให้ผู้อื่นดำเนินกำรแทน แนบใบมอบฉันทะ พร้อมสำเนำบัตรประชาชนของผู้มอบและผู้รับมอบ");
                    contentStream.endText();

                    contentStream.beginText();
                    contentStream.setFont(thaiFont, 18);
                    contentStream.newLineAtOffset(90, 385);
                    contentStream.showText("In case of request or receive by the proxy, Please attach a letter of authorization and a copy of ID card both the authorization and the proxy");
                    contentStream.endText();
                } else if (requestFor.equals("เปลี่ยนชื่อตัว")) {
                    String fromT = from.split(" ")[1];
                    String fromE = from.split(" ")[0];
                    String toT = to.split(" ")[1];
                    String toE = to.split(" ")[0];
                    contentStream.beginText();
                    contentStream.setFont(thaiFontBold, 18);
                    contentStream.newLineAtOffset(90, 475);
                    contentStream.showText(" เปลี่ยนชื่อตัว (แนบใบเปลี่ยนชื่อ)");
                    contentStream.setFont(thaiFont, 16);
                    contentStream.showText(" Change of First name (Attach the officially approved change of name document)");
                    contentStream.endText();

                    contentStream.beginText();
                    contentStream.setFont(thaiFont, 18);
                    contentStream.newLineAtOffset(110, 460);
                    contentStream.showText(" จาก...................................................................");
                    contentStream.showText(" เป็น...................................................................");
                    contentStream.endText();
                    contentStream.beginText();
                    contentStream.setFont(thaiFont, 18);
                    contentStream.newLineAtOffset(135, 464);
                    contentStream.showText("       "+fromT);
                    contentStream.showText("                                              " + toT);
                    contentStream.endText();

                    contentStream.beginText();
                    contentStream.setFont(thaiFont, 18);
                    contentStream.newLineAtOffset(110, 445);
                    contentStream.showText(" From...................................................................");
                    contentStream.showText(" To...................................................................");
                    contentStream.endText();

                    contentStream.beginText();
                    contentStream.setFont(thaiFont, 18);
                    contentStream.newLineAtOffset(140, 449);
                    contentStream.showText("   "+fromE);
                    contentStream.showText("                                                 " + toE);
                    contentStream.endText();

                } else if (requestFor.equals("เปลี่ยนชื่อสกุล")) {
                    String fromT = from.split(" ")[1];
                    String fromE = from.split(" ")[0];
                    String toT = to.split(" ")[1];
                    String toE = to.split(" ")[0];
                    contentStream.beginText();
                    contentStream.setFont(thaiFontBold, 18);
                    contentStream.newLineAtOffset(90, 475);
                    contentStream.showText(" เปลี่ยนชื่อสกุล (แนบใบเปลี่ยนชื่อสกุล)");
                    contentStream.setFont(thaiFont, 16);
                    contentStream.showText(" Change of Last name (Attach the officially approved change of surname document)");
                    contentStream.endText();

                    contentStream.beginText();
                    contentStream.setFont(thaiFont, 18);
                    contentStream.newLineAtOffset(110, 460);
                    contentStream.showText(" จาก...................................................................");
                    contentStream.showText(" เป็น...................................................................");
                    contentStream.endText();
                    contentStream.beginText();
                    contentStream.setFont(thaiFont, 18);
                    contentStream.newLineAtOffset(135, 464);
                    contentStream.showText("       "+fromT);
                    contentStream.showText("                                              " + toT);
                    contentStream.endText();

                    contentStream.beginText();
                    contentStream.setFont(thaiFont, 18);
                    contentStream.newLineAtOffset(110, 445);
                    contentStream.showText(" From...................................................................");
                    contentStream.showText(" To...................................................................");
                    contentStream.endText();

                    contentStream.beginText();
                    contentStream.setFont(thaiFont, 18);
                    contentStream.newLineAtOffset(140, 449);
                    contentStream.showText("   "+fromE);
                    contentStream.showText("                                                 " + toE);
                    contentStream.endText();

                } else if (requestFor.equals("อื่นๆ")) {
                    contentStream.beginText();
                    contentStream.setFont(thaiFont, 18);
                    contentStream.newLineAtOffset(90, 475);
                    contentStream.showText("        " + reason);
                    contentStream.endText();

                    contentStream.beginText();
                    contentStream.setFont(thaiFont, 18);
                    contentStream.newLineAtOffset(90, 470);
                    contentStream.showText("  ................................................................................................................");
                    contentStream.endText();
                }

                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 18);
                contentStream.newLineAtOffset(290, 365);
                contentStream.showText("ลงนามนิสิต/ผู้ดำเนินการแทน");
                contentStream.setFont(thaiFont, 18);
                contentStream.showText("  ................................................................");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 18);
                contentStream.newLineAtOffset(280, 350);
                contentStream.showText("Student/Person Requesting Signature");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(480, 350);
                contentStream.showText("  วันที่ ");
                contentStream.showText(headdate);
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 14);
                contentStream.newLineAtOffset(480, 330);
                contentStream.showText("Date");
                contentStream.endText();


                contentStream.addRect(55, 140, 270, 150);
                contentStream.addRect(325, 140, 280, 150);
                contentStream.stroke();

                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 16);
                contentStream.newLineAtOffset(60, 270);
                contentStream.showText("เรียน หัวหน้าภาควิชา");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(60, 255);
                contentStream.showText("To Head of department");
                contentStream.endText();

                contentStream.addRect(64, 240, 10, 10);
                contentStream.stroke();
                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(70, 240);
                contentStream.showText("  เห็นชอบ  Approved");
                contentStream.endText();

                contentStream.addRect(64, 225, 10, 10);
                contentStream.stroke();
                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(70, 225);
                contentStream.showText("  ไม่เห็นชอบ  Denied");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(80, 210);
                contentStream.showText("ลงนาม/Signature  ....................................................");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(170, 195);
                contentStream.showText("( ............................................ )");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(200, 180);
                contentStream.showText("..../...../........");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(180, 165);
                contentStream.showText("อาจารย์ที่ปรึกษา Advisor");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFontBold, 16);
                contentStream.newLineAtOffset(330, 270);
                contentStream.showText("คำพิจารณาหัวหน้าภาควิชา");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(330, 255);
                contentStream.showText("Head of department’s decision");
                contentStream.endText();

                contentStream.addRect(334, 240, 10, 10);
                contentStream.stroke();
                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(340, 240);
                contentStream.showText("  อนุมัติ  Approved");
                contentStream.endText();

                contentStream.addRect(334, 225, 10, 10);
                contentStream.stroke();
                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(340, 225);
                contentStream.showText("  ไม่อนุมัติ  Denied");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(360, 210);
                contentStream.showText("ลงนาม/Signature  ....................................................");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(450, 195);
                contentStream.showText("( ............................................ )");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(480, 180);
                contentStream.showText("..../...../........");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(thaiFont, 16);
                contentStream.newLineAtOffset(430, 165);
                contentStream.showText("หัวหน้าภาควิชา Head of department");
                contentStream.endText();
                }
                contentStream.close();

                String dateNoSlash = date.replace("/", "-");
                String dateNoColon = dateNoSlash.replace(":", "-");
                String fileDate = dateNoColon.replace(" ", "_");
                document.save("data/pdf-file-list/" + fileDate +"_"+ id +".pdf");

            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }



