package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class InstructionController {
    @FXML private TextArea instructionTextArea;

    @FXML void initialize() {
        instructionTextArea.setText(
                "การใช้งานโปรแกรมยื่นคำร้องของมหาวิทยาลัยให้นิสิตทำตามขั้นตอนดังนี้ :\n" +
                        "\n" +
                        "1. เข้าสู่ระบบ\n" +
                        "   - หากยังไม่มีบัญชีนิสิตต้องลงทะเบียนก่อนเข้าใช้งานโดยคลิกที่ปุ่ม \"ลงทะเบียน\" \n" +
                        "2. สร้างคำร้อง\n" +
                        "   - คลิกที่ปุ่มสร้างคำร้องและเลือกประเภทคำร้องที่ต้องการยื่นคำร้อง\n" +
                        "   - กรอกรายละเอียดต่าง ๆ ในคำร้อง ตรวจสอบข้อมูลที่กรอกให้ถูกต้องและกด \"ยืนยัน\" \n" +
                        "   หมายเหตุ: หากต้องแนบเอกสารเพิ่มเติมให้รวมเอกสารในไฟล์เดียวแล้วอัปโหลด\n" +
                        "3. ติดตามสถานะคำร้อง\n" +
                        "   นิสิตสามารถติดตามสถานะคำร้องได้โดยคลิกที่ปุ่ม \"ติดตามคำร้อง\" \n" +
                        "   - สามารถค้นหาคำร้องจากชื่อประเภทและสถานะของคำร้องได้ที่ช่องค้นหา\n" +
                        "   - สามารถดูรายละเอียดของคำร้องโดยคลิกเลือกจากตารางรายการคำร้อง \n" +
                        "\n" +
                        "หากนิสิตยังไม่มีอาจารย์ที่ปรึกษาจะไม่สามารถยื่นคำร้องได้ กรุณาติดต่อเจ้าหน้าที่ภาควิชา"
        );

        instructionTextArea.setEditable(false);

    }

    @FXML void onBackButtonClick() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
