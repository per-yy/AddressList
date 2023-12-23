package ui;

import person.Person;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class MainFrame extends JFrame implements ActionListener {
    //菜单选项
    private JMenuItem Add;
    private JMenuItem Delete;
    private JMenuItem Change;
    //增删改时的对话框
    private JDialog Dialog;
    //添加确认
    private JButton addEnsure;
    //删除确认
    private JButton deleteEnsure;
    //修改确认
    private JButton changeEnsure;
    //搜索确认
    private JButton searchEnsure;
    //刷新按钮
    private JButton update;
    //添加和修改联系人时的属性
    private JTextField name;
    private JTextField sex;
    private JTextField number;
    private JTextField address;
    //删、改、查联系人时的输入
    private JTextField Text;
    //删除和修改联系人时的选项组
    private JRadioButton option1;
    private JRadioButton option2;
    private ButtonGroup group;
    //搜索时的选项组
    private JComboBox<String> comboBox;

    public MainFrame() {
        initFrame();
        initMenu();
        initPanel();
        this.setVisible(true);
    }

    private void initFrame() {
        this.setTitle("通讯录");
        this.setSize(600, 400);

        //关闭拉伸
        this.setResizable(false);

        //居中
        this.setLocationRelativeTo(null);

        //关闭默认布局
        this.setLayout(null);
        //默认关闭方式
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initMenu() {
        //菜单条
        JMenuBar Bar = new JMenuBar();

        JMenu Function = new JMenu("功能");

        Add = new JMenuItem("添加联系人");
        Delete = new JMenuItem("删除联系人");
        Change = new JMenuItem("修改联系人");

        Function.add(Add);
        Function.add(Delete);
        Function.add(Change);

        Bar.add(Function);

        Add.addActionListener(this);
        Delete.addActionListener(this);
        Change.addActionListener(this);

        this.setJMenuBar(Bar);
    }

    private void initPanel() {
        this.getContentPane().removeAll();

        initFunction();
        ArrayList<Person> per = readPerson();
        JScrollPane panel = new JScrollPane(initTable(per));
        panel.setBounds(0, 20, 586, 400);

        this.add(panel);
        this.getContentPane().repaint();
    }

    private void updatePanel(ArrayList<Person> per) {
        this.getContentPane().removeAll();
        //重新初始化菜单下的功能
        initFunction();
        JScrollPane panel = new JScrollPane(initTable(per));
        panel.setBounds(0, 20, 586, 400);

        this.add(panel);
        this.getContentPane().repaint();
    }

    private JTable initTable(ArrayList<Person> per) {
        String[] colNames = {"姓名", "性别", "电话号码", "家庭住址"};
        String[][] data = new String[per.size()][4];
        for (int i = 0; i < per.size(); i++) {
            data[i][0] = per.get(i).getName();
            data[i][1] = per.get(i).getSex();
            data[i][2] = per.get(i).getPhoneNumber();
            data[i][3] = per.get(i).getAddress();
        }
        return new JTable(data, colNames);
    }

    private void initFunction() {
        JPanel panel=new JPanel();
        panel.setLayout(null);
        panel.setBounds(0,0,586,20);
        JLabel welcome=new JLabel("欢迎使用");
        welcome.setBounds(0,0,80,20);
        panel.add(welcome);

        update=new JButton("刷新");
        update.setBounds(263,0,60,20);
        update.addActionListener(this);
        panel.add(update);

        Text = new JTextField();
        Text.setBounds(425, 0, 100, 20);
        panel.add(Text);

        searchEnsure = new JButton("搜索");
        searchEnsure.setBounds(525, 0, 60, 20);
        searchEnsure.addActionListener(this);
        panel.add(searchEnsure);

        comboBox = new JComboBox<>();
        comboBox.addItem("姓名");
        comboBox.addItem("电话号码");
        comboBox.setBounds(345, 0, 80, 20);
        comboBox.addActionListener(this);
        panel.add(comboBox);
        this.add(panel);
    }

    private void addPerson() {
        Dialog = new JDialog();
        //取消默认布局
        Dialog.setLayout(null);

        Dialog.setTitle("添加联系人");
        Dialog.setSize(270, 190);

        JLabel labName = new JLabel("姓名:");
        JLabel labSex = new JLabel("性别:");
        JLabel labNumber = new JLabel("电话号码:");
        JLabel labAddress = new JLabel("家庭住址:");

        labName.setBounds(20, 20, 60, 20);
        labSex.setBounds(20, 40, 60, 20);
        labNumber.setBounds(20, 60, 60, 20);
        labAddress.setBounds(20, 80, 60, 20);

        addLabel(labName, labSex, labNumber, labAddress);

        name.setBounds(110, 20, 120, 20);
        sex.setBounds(110, 40, 120, 20);
        number.setBounds(110, 60, 120, 20);
        address.setBounds(110, 80, 120, 20);

        Dialog.add(name);
        Dialog.add(sex);
        Dialog.add(number);
        Dialog.add(address);

        addEnsure = new JButton("确定");
        addEnsure.setBounds(100, 110, 60, 30);
        Dialog.add(addEnsure);
        addEnsure.addActionListener(this);

        Dialog.setLocationRelativeTo(null);
        //不关闭弹窗无法操作下面的页面
        Dialog.setModal(true);
        Dialog.setVisible(true);
    }

    //添加和修改时重复的代码
    private void addLabel(JLabel labName, JLabel labSex, JLabel labNumber, JLabel labAddress) {
        Dialog.add(labName);
        Dialog.add(labSex);
        Dialog.add(labNumber);
        Dialog.add(labAddress);

        name = new JTextField();
        sex = new JTextField();
        number = new JTextField();
        address = new JTextField();
    }

    private void addPersonEnsure() {

        if (name.getText().isEmpty() || sex.getText().isEmpty() || number.getText().isEmpty() || address.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "输入信息不完整！");
        } else if (number.getText().length() != 11) {
            JOptionPane.showMessageDialog(null, "号码输入有误！");
        } else {
            //判断联系人是否已存在
            ArrayList<Person> per = readPerson();
            for (Person list : per) {
                if (list.getPhoneNumber().equals(number.getText())) {
                    JOptionPane.showMessageDialog(null, "该号码已存在！");
                    return;
                }
            }
            //写入
            String personInf = name.getText() + "," + sex.getText() + "," + number.getText() + "," + address.getText() + "\n";
            try {
                //不存在则创建，从末尾写入
                Files.write(Paths.get("person/person.txt"), personInf.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                JOptionPane.showMessageDialog(null, "添加成功！");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "添加失败：" + ex.getMessage());
            }
            Dialog.dispose();
        }
    }

    private void deletePerson() {
        Dialog = new JDialog();
        Dialog.setTitle("删除联系人");
        Dialog.setSize(300, 160);
        Dialog.setLocationRelativeTo(null);
        Dialog.setLayout(null);

        JLabel label = new JLabel("请输入你要删除的联系人的姓名或电话号码:");
        label.setBounds(0, 0, 250, 20);
        Dialog.add(label);

        Text = new JTextField();
        Text.setBounds(60, 25, 150, 20);
        Dialog.add(Text);

        option1 = new JRadioButton("姓名");
        option2 = new JRadioButton("电话号码");
        option1.setBounds(57, 50, 50, 20);
        option2.setBounds(134, 50, 100, 20);
        group = new ButtonGroup();
        group.add(option1);
        group.add(option2);
        Dialog.add(option1);
        Dialog.add(option2);

        deleteEnsure = new JButton("确定");
        deleteEnsure.setBounds(110, 80, 60, 30);
        deleteEnsure.addActionListener(this);
        Dialog.setModal(true);
        Dialog.add(deleteEnsure);

        Dialog.setVisible(true);
    }

    private void deletePersonEnsure() {
        ArrayList<Person> per = readPerson();
        if (Text.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "请输入姓名或者号码！");
        } else if (group.getSelection() == null) {
            JOptionPane.showMessageDialog(null, "请选择输入方式！");
        } else {

            //用于判断要删除的联系人是否存在
            boolean flag = false;
            Iterator<Person> iterator = per.iterator();
            if (option1.isSelected()) {
                //以姓名匹配删除
                while (iterator.hasNext()) {
                    Person person = iterator.next();
                    if (person.getName().equals(Text.getText())) {
                        flag = true;
                        iterator.remove();
                    }
                }
            } else if (option2.isSelected()) {
                //以号码匹配删除
                while (iterator.hasNext()) {
                    Person person = iterator.next();
                    if (person.getPhoneNumber().equals(Text.getText())) {
                        flag = true;
                        iterator.remove();
                    }
                }
            }
            if (!flag) {
                JOptionPane.showMessageDialog(null, "该联系人不存在！");
            } else {
                if (writePerson(per)) {
                    JOptionPane.showMessageDialog(null, "删除成功！");
                }
                //关闭对话框
                Dialog.dispose();
            }
        }

    }

    private void changePerson() {
        Dialog = new JDialog();
        Dialog.setTitle("修改联系人");
        Dialog.setSize(280, 240);
        Dialog.setLocationRelativeTo(null);
        Dialog.setLayout(null);

        Dialog.setModal(true);

        JLabel label = new JLabel("请输入你要修改的联系人的姓名或电话号码:");
        label.setBounds(0, 0, 250, 20);
        Dialog.add(label);

        Text = new JTextField();
        Text.setBounds(0, 20, 100, 20);
        Dialog.add(Text);

        option1 = new JRadioButton("姓名");
        option2 = new JRadioButton("电话号码");
        option1.setBounds(110, 20, 50, 20);
        option2.setBounds(165, 20, 80, 20);
        group = new ButtonGroup();
        group.add(option1);
        group.add(option2);
        Dialog.add(option1);
        Dialog.add(option2);

        JLabel afterChange = new JLabel("请输入修改后的联系人信息:");
        afterChange.setBounds(0, 50, 200, 20);
        Dialog.add(afterChange);

        JLabel labelName = new JLabel("姓名:");
        JLabel labelSex = new JLabel("性别:");
        JLabel labelNumber = new JLabel("电话号码:");
        JLabel labelAddress = new JLabel("家庭住址:");

        labelName.setBounds(20, 70, 50, 20);
        labelSex.setBounds(20, 90, 50, 20);
        labelNumber.setBounds(20, 110, 80, 20);
        labelAddress.setBounds(20, 130, 80, 20);

        addLabel(labelName, labelSex, labelNumber, labelAddress);

        name.setBounds(100, 70, 100, 20);
        sex.setBounds(100, 90, 100, 20);
        number.setBounds(100, 110, 100, 20);
        address.setBounds(100, 130, 100, 20);
        Dialog.add(name);
        Dialog.add(sex);
        Dialog.add(number);
        Dialog.add(address);

        changeEnsure = new JButton("确定");
        changeEnsure.setBounds(110, 160, 60, 30);
        changeEnsure.addActionListener(this);
        Dialog.add(changeEnsure);

        Dialog.setVisible(true);
    }

    private void changePersonEnsure() {
        ArrayList<Person> per = readPerson();
        if (Text.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "请输入姓名或者电话号码！");
        } else if (group.getSelection() == null) {
            JOptionPane.showMessageDialog(null, "请选择输入方式！");
        } else if (name.getText().isEmpty() || sex.getText().isEmpty() || number.getText().isEmpty() || address.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "请输入完整的修改后的联系人信息！");
        } else {
            Iterator<Person> iterator = per.iterator();
            boolean flag = false;
            if (option1.isSelected()) {
                //匹配姓名
                while (iterator.hasNext()) {
                    Person person = iterator.next();
                    if (person.getName().equals(Text.getText())) {
                        person.setName(name.getText());
                        person.setSex(sex.getText());
                        person.setPhoneNumber(number.getText());
                        person.setAddress(address.getText());
                        flag = true;
                    }
                }
            } else if (option2.isSelected()) {
                //匹配号码
                while (iterator.hasNext()) {
                    Person person = iterator.next();
                    if (person.getPhoneNumber().equals(Text.getText())) {
                        person.setName(name.getText());
                        person.setSex(sex.getText());
                        person.setPhoneNumber(number.getText());
                        person.setAddress(address.getText());
                        flag = true;
                    }
                }
            }
            if (!flag) {
                JOptionPane.showMessageDialog(null, "该联系人不存在！");
            } else {
                if (writePerson(per)) {
                    JOptionPane.showMessageDialog(null, "修改成功！");
                }
                //关闭对话框
                Dialog.dispose();
            }
        }
    }

    private void searchPerson() {
        ArrayList<Person> per = readPerson();
        ArrayList<Person> afterSearch = new ArrayList<>();
        if (Objects.equals(comboBox.getSelectedItem(), "姓名")) {
            for (Person list : per) {
                if (list.getName().equals(Text.getText())) {
                    afterSearch.add(list);
                }
            }
        } else if (Objects.equals(comboBox.getSelectedItem(), "电话号码")) {
            for (Person list : per) {
                if (list.getPhoneNumber().equals(Text.getText())) {
                    afterSearch.add(list);
                }
            }
        }
        updatePanel(afterSearch);
    }

    private ArrayList<Person> readPerson() {
        ArrayList<Person> per = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("person/person.txt"));
            String[] parts;
            for (String line : lines) {
                parts = line.split(",");
                //去掉末尾换行符
                per.add(new Person(parts[0], parts[1], parts[2], parts[3].replace("\n", "")));
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "读取异常：" + ex.getMessage());
        }
        return per;

    }
    //修改和删除时的重新写入
    private boolean writePerson(ArrayList<Person> per) {
        StringBuilder allPerson = new StringBuilder();
        for (Person list : per) {
            String personInf = list.getName() + "," + list.getSex() + "," + list.getPhoneNumber() + "," + list.getAddress() + "\n";
            allPerson.append(personInf);
        }
        //不存在则创建，覆盖写
        try {
            FileWriter fw = new FileWriter("person/person.txt");
            fw.write(allPerson.toString());
            fw.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "写入失败！" + ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == searchEnsure) {
            searchPerson();
        } else if (obj == Add) {
            addPerson();
        } else if (obj == Delete) {
            deletePerson();
        } else if (obj == Change) {
            changePerson();
        } else if (obj == addEnsure) {
            addPersonEnsure();
        } else if (obj == deleteEnsure) {
            deletePersonEnsure();
        } else if (obj == changeEnsure) {
            changePersonEnsure();
        }else if(obj==update){
            initPanel();
        }
    }
}
