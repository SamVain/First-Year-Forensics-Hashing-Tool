import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.*;


public class Main {

    private JFrame frame;
    private JTextField textFieldin;
    private JTextField textFieldOutSHA1;
    private JTextField textFieldOutSHA256;
    private JTextField textFieldOutMD5;
    private JCheckBox ckbtnSha1;
    private JCheckBox ckbtnMd5;
    private JCheckBox ckbtnSha256;
    private JComboBox<String> comboBox;
    private JButton btnHash;
    private JButton btnOfd; //OpenFileDialog
    private File file;
    private String [][] hashes;
    
    /**
     * Launch the application.
     */
    
     public static void main(String[] args)
     {
         
        EventQueue.invokeLater(new Runnable()
        
        {
            
            public void run()
            
            {
                try {
                    
                    Main window = new Main();
                    
                    window.frame.setVisible(true);
                
                }
                
                catch (Exception e) 
                
                {
                    
                    e.printStackTrace();
                
                }
            
            }
        
        });
    
    }
    
    public Main() throws NoSuchAlgorithmException
    
    {
        
        initialize();
    
    }
    
    private void WriteHashesToFile()
    
    {
        
        try {
            
            FileWriter outFile = new FileWriter( "C:\\Test Files For Hashing\\hashes.txt", true );
            PrintWriter writer = new PrintWriter( outFile );
            writer.println(file.getPath());
            writer.println(hashes[0][0] + " : " + hashes[1][0]);
            writer.println(hashes[0][1] + " : " + hashes[1][1]);
            writer.println(hashes[0][2] + " : " + hashes[1][2]);
            writer.close();
            outFile.close();
        
        } catch (IOException e) {
            
            e.printStackTrace();
        
        }
    
    }
    
    private String[] CompareHash(String hash) {
        
        BufferedReader reader;
        String filename = null;
        String sha1 = null;
        String sha256 = null;
        String md5 = null;
        String [] match = null;
        
        try {
            
            reader = new BufferedReader(new FileReader("C:\\Test Files For Hashing\\KnownHashes.txt"));
            
            filename = reader.readLine();
            
            if (filename != null) {
                
                sha1 = reader.readLine().split(" : ")[1];
                
                sha256 = reader.readLine().split(" : ")[1];
                
                md5 = reader.readLine().split(" : ")[1];
            
            }
            
            while (filename != null) {
                
                if (hash.equals(sha1))
                
                {
                    
                    match = new String[2];
                    
                    match[0] = filename;
                    
                    match[1] = "SHA-1";
                    
                    break;
                
                }
                
                if (hash.equals(sha256))
                {
                    
                    match = new String[2];
                    
                    match[0] = filename;
                    
                    match[1] = "SHA-256";
                    
                    break;
                
                }
                
                if (hash.equals(md5))
                
                {
                    
                    match = new String[2];
                    
                    match[0] = filename;
                    
                    match[1] = "MD5";
                    
                    break;
                
                }
                
                // read next line
                
                filename = reader.readLine();
                
                if (filename != null) {
                    
                    sha1 = reader.readLine().split(" : ")[1];
                    
                    sha256 = reader.readLine().split(" : ")[1];
                    
                    md5 = reader.readLine().split(" : ")[1];
                
                }
            }
            
            reader.close();
        
        } catch (IOException e) {
            
            e.printStackTrace();
        
        }
        
        return match;
    
    }
    
    private String DoHash(String hashType) {
        
        byte byteData[] = null;
        
        MessageDigest md = null;
        
        try {
            
            md = MessageDigest.getInstance(hashType);
            
            if (comboBox.getSelectedIndex() == 0)
            
            {
                
                // String
                
                String password = textFieldin.getText();
                
                md.update(password.getBytes());
            
            } else {
                
                // File
                
                FileInputStream fis2 = new FileInputStream(file.getPath());
                
                byte dataBytes[] = new byte[1024];
                
                int nread = 0;
                
                while((nread = fis2.read(dataBytes)) != -1)
                
                {
                    
                    md.update(dataBytes, 0, nread);
                
                };
                
                fis2.close();
            
            }
            
            byteData = md.digest();
        
        } catch (NoSuchAlgorithmException e) {
            
            e.printStackTrace();
        
        } catch (FileNotFoundException e) {
            
            e.printStackTrace();
        
        } catch (IOException e) {
            
            e.printStackTrace();
        
        }
        
        StringBuffer hexString = new StringBuffer();
        
        for (int i=0; i<byteData.length; i++)
        
        {
            
            String hex=Integer.toHexString( byteData[i] & 0xff );
            
            if(hex.length()==1)
            
            hexString.append('0');
            
            hexString.append(hex);
        
        }
        
        return hexString.toString();
    
    }
    
    
    private void initialize() throws NoSuchAlgorithmException {
        
        frame = new JFrame();
        
        frame.setBounds(100, 100, 900, 700);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.getContentPane().setLayout(null);
        
        hashes = new String[2][3];
        
        hashes[0][0] = "SHA-1";
        
        hashes[0][1] = "SHA-256";
        
        hashes[0][2] = "MD5";
        
        // Add Hash button to form
        
        btnHash = new JButton("Hash!");
        
        btnHash.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent arg0)
            
            {
                
                if (ckbtnSha1.isSelected())
                
                {
                    
                    hashes[1][0] = DoHash(hashes[0][0]);
                    
                    textFieldOutSHA1.setText(hashes[1][0]);
                
                }
                
                if(ckbtnSha256.isSelected())
                
                {
                    
                    hashes[1][1] = DoHash(hashes[0][1]);
                    
                    textFieldOutSHA256.setText(hashes[1][1]);
                
                }
                
                if (ckbtnMd5.isSelected())
                
                {
                    
                    hashes[1][2] = DoHash(hashes[0][2]);
                    
                    textFieldOutMD5.setText(hashes[1][2]);
                
                }
                
                if (comboBox.getSelectedIndex() > 0)
                
                {
                    
                    String [] retVal = CompareHash(hashes[1][0]);
                    
                    WriteHashesToFile();
                    
                    if (retVal != null)
                    
                    JOptionPane.showMessageDialog (null, retVal[0],"File match found", JOptionPane.WARNING_MESSAGE);
                }
            
            }
        
        });
        
        btnHash.setBounds(483, 396, 89, 23);
        
        frame.getContentPane().add(btnHash);
        
        // Add file browse button to form
        
        btnOfd = new JButton("Browse");
        
        btnOfd.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent arg0)
            
            {
                
                JFileChooser fc = new JFileChooser("C:\\Test Files For Hashing\\Files To Be Hashed (Evidence)");
                
                //Handle open button action.
                
                if (arg0.getSource() == btnOfd) {
                    
                    int returnVal = fc.showOpenDialog(frame);
                    
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        
                        file = fc.getSelectedFile();
                        
                        textFieldin.setText(file.getPath());
                    
                    } else {


                    }
                
                }
            
            }
        
        });
        
        btnOfd.setBounds(600, 86, 89, 20);
        
        btnOfd.setVisible(false);
        
        frame.getContentPane().add(btnOfd);
        
        // Add text field to accept input from user to form
        
        textFieldin = new JTextField();
        
        textFieldin.setBounds(158, 86, 414, 20);
        
        frame.getContentPane().add(textFieldin);
        
        textFieldin.setColumns(10);
        
        JLabel lblWelcome = new JLabel("Welcome to my Hashing Program");
        
        lblWelcome.setBounds(10, 11, 246, 14);

        frame.getContentPane().add(lblWelcome);
        
        JLabel lbl2ndLn = new JLabel("Here you can hash a String or File of your choosing. ");
        
        lbl2ndLn.setBounds(10, 36, 330, 14);
        
        frame.getContentPane().add(lbl2ndLn);
        
        JLabel lbl3rdLn = new JLabel("Just enter the String or File Location of your choosing below and press Hash!");
        
        lbl3rdLn.setBounds(10, 61, 485, 14);
        
        frame.getContentPane().add(lbl3rdLn);
        
        comboBox = new JComboBox<String>();
        
        comboBox.addActionListener(new ActionListener()
        
        {
            
            public void actionPerformed(ActionEvent arg0)
            
            {
                
                //COMBOBOX ACTION
                
                if(comboBox.getSelectedIndex() == 0)
                
                {
                    
                    btnOfd.setVisible(false);
                
                } else {
                    
                    btnOfd.setVisible(true);
                
                }
            
            }
        
        });
        
        comboBox.setBounds(20, 86, 101, 20);
        
        frame.getContentPane().add(comboBox);
        
        textFieldOutSHA1 = new JTextField();
        
        textFieldOutSHA1.setEditable(false);
        
        textFieldOutSHA1.setBounds(102, 140, 470, 20);
        
        frame.getContentPane().add(textFieldOutSHA1); //
        
        textFieldOutSHA1.setColumns(10);
        
        textFieldOutSHA256 = new JTextField();

        textFieldOutSHA256.setEditable(false);
        
        textFieldOutSHA256.setColumns(10);
        
        textFieldOutSHA256.setBounds(102, 211, 470, 20);
        
        frame.getContentPane().add(textFieldOutSHA256);
        
        textFieldOutMD5 = new JTextField();
        
        textFieldOutMD5.setEditable(false);
        
        textFieldOutMD5.setColumns(10);
        
        textFieldOutMD5.setBounds(102, 287, 470, 20);
        
        frame.getContentPane().add(textFieldOutMD5);
        
        ckbtnSha1 = new JCheckBox("SHA-1");
        
        ckbtnSha1.setBounds(20, 139, 109, 23);
        
        frame.getContentPane().add(ckbtnSha1);
        
        ckbtnMd5 = new JCheckBox("MD5");
        
        ckbtnMd5.setBounds(20, 286, 109, 23);
        
        frame.getContentPane().add(ckbtnMd5);
        
        ckbtnSha256 = new JCheckBox("SHA-256");
        
        ckbtnSha256.setBounds(20, 210, 109, 23);
        
        frame.getContentPane().add(ckbtnSha256);
        
        comboBox.addItem("String");
        
        comboBox.addItem("File Hashing");
    
    }

}


