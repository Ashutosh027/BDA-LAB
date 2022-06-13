import json
from PyQt5 import QtWidgets
from PyQt5.QtWidgets import *
from PyQt5.QtGui import QIntValidator, QFont
from PyQt5.QtCore import Qt
import sys


class StudentInfo(QTabWidget):
    def __init__(self, parent=None):
        super(StudentInfo, self).__init__(parent)
        self.d = None
        self.flo = None
        self.name = QLabel()
        self.semester = QLabel()
        self.branch = QLabel()
        self.errMsg = QLabel()
        self.layout2 = None
        self.usn = None
        self.e4 = None
        self.e3 = None
        self.e2 = None
        self.e1 = None
        self.tab1 = QWidget()
        self.tab2 = QWidget()
        self.used = False
        self.addTab(self.tab1, "Enter New")
        self.addTab(self.tab2, "View Previous")
        self.tab1UI()
        self.tab2UI()
        self.setWindowTitle("Student Info")

    def viewButton(self):
        usn = self.usn.text()
        self.errMsg.setText("")
        with open('stuInfo.json', mode='r', encoding='utf-8') as fp2:
            data = json.load(fp2)
            lst = [self.name, self.semester, self.branch]

            if self.used:
                for i in lst:
                    i.setText("None")
            else:
                self.layout2.addRow("Name:", self.name)
                self.layout2.addRow("Semester:", self.semester)
                self.layout2.addRow("Branch:", self.branch)

            if usn not in data:
                self.errMsg.setText("USN Doesn't Exist!")
            else:
                self.used = True
                self.name.setText(data[usn]['Name'])
                self.semester.setText(data[usn]['Semester'])
                self.branch.setText(data[usn]['Branch'])

    def handleButton(self):
        name = self.e1.text()
        usn = self.e2.text()
        sem = self.e3.text()
        branch = self.e4.text()
        with open('stuInfo.json', mode='r+', encoding='utf-8') as fp:
            data = {usn: {"Name": name,
                          "Semester": sem,
                          "Branch": branch}}
            old = json.load(fp)
            if usn in old:
                self.showdialog()
            else:
                old.update(data)
                fp.seek(0)
                json.dump(old, fp, indent=4)
        sys.exit()

    def showdialog(self):
        self.d = QDialog()
        l1 = QLabel("USN already Exists!!", self.d)
        b1 = QPushButton("ok", self.d)
        b1.move(100, 80)
        b1.clicked.connect(self.closeIt)
        self.d.setWindowTitle("Dialog")
        self.d.setWindowModality(Qt.ApplicationModal)
        self.d.exec_()

    def closeIt(self):
        self.d.close()

    def tab1UI(self):
        self.flo = QFormLayout()

        self.e1 = QLineEdit()
        self.e1.setFont(QFont("Arial", 14))

        self.e2 = QLineEdit()
        self.e2.setMaxLength(10)
        self.e2.setFont(QFont("Arial", 14))

        self.e3 = QLineEdit()
        self.e3.setValidator(QIntValidator())
        self.e3.setFont(QFont("Arial", 14))

        self.e4 = QLineEdit()
        self.e4.setFont(QFont("Arial", 14))

        self.flo.addRow("Name:", self.e1)
        self.flo.addRow("USN:", self.e2)
        self.flo.addRow("Sem:", self.e3)
        self.flo.addRow("Branch:", self.e4)

        button1 = QtWidgets.QPushButton('submit')
        button1.setText("Submit")
        button1.clicked.connect(self.handleButton)

        self.flo.addWidget(button1)
        self.setLayout(self.flo)
        self.setWindowTitle("Student Info")
        self.tab1.setLayout(self.flo)

    def tab2UI(self):
        button2 = QtWidgets.QPushButton('view')
        button2.setText("View")
        button2.clicked.connect(self.viewButton)

        self.layout2 = QFormLayout()
        self.usn = QLineEdit()
        self.usn.setMaxLength(10)
        self.layout2.addRow("USN", self.usn)
        self.layout2.addWidget(button2)
        self.layout2.addWidget(self.errMsg)
        self.tab2.setLayout(self.layout2)


def main():
    app = QApplication(sys.argv)
    ex = StudentInfo()
    ex.show()
    sys.exit(app.exec_())


if __name__ == '__main__':
    main()
