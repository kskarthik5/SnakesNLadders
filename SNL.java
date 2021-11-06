import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class SNL extends JFrame {
    private JFrame f;
    private String players[] = {"Red","Blue","Green","Yellow"};
    private JPanel jp;
    private JButton sb;
    private JComboBox<String> cb;
    private JButton rb;
    private JButton ng;
    private JButton ex;
    private JTextPane ge;
    private JTextPane rs;
    private int cp=0;
    private int rolled=-1;
    private JLabel[][] items;
    private int n;
    private int pp[];
    private int indices[][]=new int[100][2];
    private String winners="";
    private JTextPane ws;
    private SNL()throws IOException  {
        f = new JFrame("Snakes n Ladders");
        f.setLayout(new BorderLayout());
        f.setContentPane(new JLabel(new ImageIcon("./res/board.png")));
        String pnos[] = { "2 Players", "3 Players", "4 Players" };
        cb = new JComboBox<String>(pnos);// No. of Players Dropdown menu
        cb.setBounds(50, 20, 100, 30);
        sb = new JButton("BEGIN");// Begin button
        ge=new JTextPane();
        ge.setBounds(620, 300, 100, 100);
        ge.setFont(ge.getFont().deriveFont(20f));
        ge.setEditable(false);
        rb=new JButton("Roll");
        rs=new JTextPane();
        rs.setBounds(620,150,100,100);
        rs.setFont(rs.getFont().deriveFont(20f));
        rs.setEditable(false);
        rb.setBounds(620, 400, 100, 30);
        sb.setBounds(200, 20, 100, 30);
        ws=new JTextPane();
        ws.setBounds(250, 20, 400, 35);
        ws.setFont(ge.getFont().deriveFont(20f));
        ws.setEditable(false); 
        jp= new JPanel(new GridLayout(10,10)); 
        items=new JLabel[10][10];
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){       
                items[i][j]=new JLabel();
                jp.add(items[i][j]);
            }
        }
        int k=100;
        indices[0][0]=-1;
        indices[0][1]=-1;
        for(int i=0;i<10;i++){
            if(k<0)
            break;
            for(int j=0;j<10;j++){
                k--;
                indices[k][0]=i;
                indices[k][1]=j;
            }
            i++;
            if(k<0)
            break;
            for(int j=9;j>=0;j--){
                k--;
                indices[k][0]=i;
                indices[k][1]=j;
            }
        }
        for(int i=0;i<100;i++)
        System.out.println(indices[i][0]+" "+indices[i][1]);
        jp.setBounds(51,67 , 495, 495);
        jp.setBackground(new Color(0, 0, 0, 0));
        ng=new JButton("RESTART");
        ng.setBounds(570, 500, 100, 30);
        ex=new JButton("EXIT");
        ex.setBounds(680, 500, 100, 30);
        f.add(cb);
        f.add(sb);
        f.setSize(599,799);
        f.setSize(800,600);
        f.setVisible(true);
        f.setResizable(false);
        f.add(ng);
        f.add(ex);
        f.revalidate();
        f.repaint();
        ex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                f.dispose(); 
                f.setVisible(false);
                System.exit(0);
            }});
        ng.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    f.dispose(); 
                    f.setVisible(false);
                    try {
                        new SNL();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }});
        sb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                run();
            }});
    }   
    public static void main(String[] args)throws IOException  {
        new SNL();
    }

    private static int roll(){
        List<Integer> list = Arrays.asList(1,2,3,4,5,6);
        Collections.shuffle(list);
        return list.get(0);
    }

    private void run(){
        f.add(ge);
        f.remove(sb);
        f.add(rb);
        f.remove(cb);
        f.add(rs);
        n=Integer.parseInt((cb.getItemAt(cb.getSelectedIndex())).toString().charAt(0)+"");
        ge.setText(players[cp]+"'s turn");
        pp=new int[n];
        f.revalidate();
        f.repaint();
        for(int i=0;i<n;i++)
            pp[i]=0;
        paint(pp,n);
        rb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                rolled=roll();
                rs.setText(players[cp]+" rolled a "+rolled);
                f.revalidate();
                f.repaint();
                if(pp[cp]+rolled<100)
                pp[cp]=pp[cp]+rolled;
                pp[cp]=ifOnSnake(pp[cp]);
                pp[cp]=ifOnLadder(pp[cp]);
                System.out.println(pp[cp]+" "+cp);
                paint(pp,n);
                if(pp[cp]==99)
                {
                    ge.setText(players[cp]+" won.");
                    pp[cp]=-77;
                    winners=winners+" > "+players[cp];                 
                    ws.setText("Winners = "+winners);
                    f.add(ws);
                    f.revalidate();
                    f.repaint();
                }
                
                if(rolled<6){
                    changeTurn();
                }
                if(checkGameOver()){
                    f.remove(rb);
                    return;
                }
                skipWinners();
                ge.setText(players[cp]+"'s turn");
                f.revalidate();
                f.repaint();              
            }});
    }
    private boolean checkGameOver() {
        int count=0;
        int loser=-1;
        for(int i=0;i<n;i++){
            if(pp[i]!=-77)
                count++;
        }
        if(count==1)
        {
            for(int i=0;i<n;i++)
            {
                if(pp[i]!=-77){
                    loser=i;
                    break;
                }
            }
            ge.setText(players[loser]+" Lost. Game Over");
            f.remove(rs);
            f.revalidate();
            f.repaint();  
            return true;
        }
        else{
            return false;
        }
    }
    private void changeTurn(){
                    cp++;
                    if(cp>=n){
                    cp=0;
                    }
    }
    private void skipWinners(){
        if(pp[cp]==-77){
            changeTurn();
            skipWinners();
        }
    }
    private int ifOnLadder(int i) {
        if(i==18)
            return 75;
        else if(i==7)
            return 54;
        else if(i==31)
            return 91;  
        else
            return i;
    }

    private int ifOnSnake(int i) {
        if(i==96)
            return 58;
        else if(i==87)
            return 32;
        else if(i==55)
            return 4;  
        else
            return i;
    }
    private void paint(int pp[],int n){
        try {
            jp.removeAll();
            jp.setBounds(51,67 , 495, 495);
            jp.setBackground(new Color(0, 0, 0, 0));
            items=new JLabel[10][10];
            for(int a=0;a<10;a++){
                for(int b=0;b<10;b++){   
                    int flag=0;
                    inner:for(int k=0;k<n;k++)
                    {
                        if(pp[k]==-77)
                            continue inner;
                        int i=indices[pp[k]][0];
                        int j=indices[pp[k]][1];
                        items[a][b]=new JLabel();
                        if(a==i&&b==j)
                        {
                            BufferedImage myPicture = ImageIO.read(new File("./res/"+players[k]+".png"));    
                            items[a][b].setIcon(new ImageIcon(myPicture));   
                            jp.add(items[a][b]);
                            flag=1;
                            break inner;                            
                        }
                    }
                    if(flag==0)
                    jp.add(items[a][b]);
                }
            }
            f.add(jp);
            f.revalidate();
            f.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
