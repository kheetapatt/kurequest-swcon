# CS211 Project ภาคต้น 2567

## วิธีการติดตั้งและรันโปรแกรม
วิธีการติดตั้งโปรแกรม
1. ไปที่โฟลเดอร์ “release” ใน GitHub Repository
2. เปิดโฟลเดอร์ “release” แล้วคลิกเลือกไฟล์ “kurequest.zip”
3. เมื่อเข้าไปที่ไฟล์ “kurequest.zip” แล้วกดดาวน์โหลด ดังภาพด้านล่าง\
   ![Example Image](https://drive.google.com/uc?id=1V6rIHQPcuv71YVHBoXzoZZqAC7Qs0WbE)
4. เมื่อดาวน์โหลดเสร็จแล้วให้เข้าไปที่ที่ไฟล์ ZIP “kurequest” ถูกเก็บในเครื่อง
5. คลิกขวาที่ไฟล์แล้วเลือก “Extract All…” ดังภาพด้านล่าง จากนั้นจะปรากฏโฟลเดอร์ “kurequest”\
   ![Example Image](https://drive.google.com/uc?id=1QUCbGokiaF1XK8OXseqrC9ybPF6JNhrO)


วิธีการรันโปรแกรม
1. เปิด command line tool เช่น Command Prompt
2. เปลี่ยน path เพื่อไปยัง directory “kurequest” โดยใช้คำสั่ง cd เช่น

   ```cd Downloads/kurequest```
3. รันโปรแกรมโดยใช้คำสั่ง

   ```java -jar KURequest-1.0-shaded.jar```

ที่อยู่ของไฟล์ pdf รายละเอียดและการใช้งานโปรแกรมอยู่ใน directory release/application-detail

## ตัวอย่างข้อมูลผู้ใช้ระบบ (username, password) 
ผู้ดูแลระบบ\
username : kureq_admin\
password : admin0000


เจ้าหน้าที่คณะ\
username : sc01\
password : anchisa123


เจ้าหน้าที่ภาควิชา\
username : cs01\
password : jaruwan123


อาจารย์ที่ปรึกษา\
username : ad_cs01\
password : rattanaporn123


นิสิต\
username : b6710450001\
password : pornpen123



## การวางโครงสร้างไฟล์ของโครงงาน
**data** : เก็บข้อมูลของผู้ใช้ คณะ ภาควิชา คำร้อง และผู้อนุมัติคำร้อง เป็นไฟล์ csv\
**user-images** : เก็บไฟล์รูปภาพของผู้ใช้ทั้งหมด\
**pdf-file-list** : เก็บไฟล์ pdf ที่เกี่ยวกับคำร้องทั้งหมด\
**src/main** : เก็บทุกอย่างของแอปพลิเคชัน\
**controllers** : เก็บคลาสที่ทำหน้าที่เชื่อมต่อกับไฟล์ fxml\
**models** : เก็บคลาสที่ทำหน้าที่เป็นโครงสร้างของ Object ในแอปพลิเคชัน\
**services** : เก็บคลาสต่าง ๆ ที่จะถูกเรียกใช้งานเพิ่มเติม\
**resource** : เก็บข้อมูลที่แอปพลิเคชันจำเป็นต้องใช้\
**font** : เก็บไฟล์ฟ้อนต์ภาษาไทยที่ถูกใช้ในการสร้างไฟล์ pdf ของใบคำร้อง\
**images** : เก็บรูปภาพที่ใช้ในแอปพลิเคชัน\
**views** : เก็บไฟล์ fxml ที่เป็น Interface ของแอปพลิเคชัน
