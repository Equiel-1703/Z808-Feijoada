package z808_gui;

import z808_gui.utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class MainGUI extends JFrame {
    // IMGs paths
    private static final String PLAY_DEFAULT_IMG_PATH = "src/main/java/z808_gui/imgs/graoFeijao.png";
    private static final String PLAY_ACTIVE_IMG_PATH = "src/main/java/z808_gui/imgs/graoFeijaoActive.png";
    private static final String Z808_FEIJOADA_LOGO_IMG_PATH = "src/main/java/z808_gui/imgs/Z808FEIJOADALOGO.png";
    private static final String STEP_DEFAULT_IMG_PATH = "src/main/java/z808_gui/imgs/stepFeijao.png";
    private static final String STEP_ACTIVE_IMG_PATH = "src/main/java/z808_gui/imgs/stepFeijaoActive.png";

    // Play and Step icon
    private static final int CONTROLS_BUTTON_SIZE = 60;
    private static final ImageIcon PLAY_DEFAULT_IMG = new ImageIcon(UIUtils.resizeImage(PLAY_DEFAULT_IMG_PATH, CONTROLS_BUTTON_SIZE, CONTROLS_BUTTON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon PLAY_HOVER_IMG = new ImageIcon(UIUtils.resizeImage(PLAY_ACTIVE_IMG_PATH, CONTROLS_BUTTON_SIZE, CONTROLS_BUTTON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon STEP_DEFAULT_IMG = new ImageIcon(UIUtils.resizeImage(STEP_DEFAULT_IMG_PATH, CONTROLS_BUTTON_SIZE, CONTROLS_BUTTON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon STEP_ACTIVE_IMG = new ImageIcon(UIUtils.resizeImage(STEP_ACTIVE_IMG_PATH, CONTROLS_BUTTON_SIZE, CONTROLS_BUTTON_SIZE, Image.SCALE_SMOOTH));

    // Logo Z808
    private static final int FEIJOADA_LOGO_SIZE_X = 455;
    private static final int FEIJOADA_LOGO_SIZE_Y = 75;

    // Path to active program
    private static String pathProgramaZ808 = "";

    // Create basic window
    public MainGUI() {
        setTitle("Z808 - Feijoada Edition");
        setIconImage((new ImageIcon(PLAY_DEFAULT_IMG_PATH)).getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLayout(new BorderLayout());
        setBackground(Color.white);

        // Appear in center
        setLocationRelativeTo(null);
    }

    // Menu bar
    private static JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Itens da barra de menus
        JMenu arquivoMenu = new JMenu("Arquivo");
        JMenu executarMenu = new JMenu("Executar");
        JMenu ajudaMenu = new JMenu("Ajuda");

        // --------------------- Sub-itens menu Arquivo ---------------------
        JMenuItem abrirMenItem = new JMenuItem("Abrir");
        // Ação do menu Arquivo -> Abrir
        abrirMenItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                pathProgramaZ808 = selectedFile.getAbsolutePath();
                System.out.println("Selected file: " + pathProgramaZ808);
            }
        });

        JMenuItem sairMenItem = new JMenuItem("Sair");
        sairMenItem.addActionListener(e -> System.exit(0));

        arquivoMenu.add(abrirMenItem);
        arquivoMenu.add(sairMenItem);

        // --------------------- Sub-itens menu Executar ---------------------
        JMenuItem executarTudoMenItem = new JMenuItem("Executar tudo");
        JMenuItem executarInstMenItem = new JMenuItem("Executar instrução");

        executarMenu.add(executarTudoMenItem);
        executarMenu.add(executarInstMenItem);

        // --------------------- Sub-itens menu Sobre ---------------------
        JMenuItem devsMenItem = new JMenuItem("Desenvolvedores");
        JMenuItem sobreMenItem = new JMenuItem("Sobre");
        JMenuItem comoUsarMenItem = new JMenuItem("Como usar");
        comoUsarMenItem.addActionListener(e -> JOptionPane.showMessageDialog(null, "Te vira.", "Como usar", JOptionPane.INFORMATION_MESSAGE, null));

        ajudaMenu.add(devsMenItem);
        ajudaMenu.add(sobreMenItem);
        ajudaMenu.add(new JSeparator());
        ajudaMenu.add(comoUsarMenItem);

        // Adicionando itens da barra principal
        menuBar.add(arquivoMenu);
        menuBar.add(executarMenu);
        menuBar.add(ajudaMenu);

        return menuBar;
    }

    public static void main(String[] args) {
        MainGUI z808UI = new MainGUI();

        // Barra de menus
        z808UI.setJMenuBar(createMenuBar());

        // Spacers
        final Dimension H_SPACER = new Dimension(10, 0);

        // Painel superior com o título
        JPanel upperTitle = new JPanel();
        upperTitle.setPreferredSize(new Dimension(0, 100));
        upperTitle.setBackground(new Color(0x5a473d));
        upperTitle.setLayout(new BoxLayout(upperTitle, BoxLayout.LINE_AXIS));
        upperTitle.add(Box.createRigidArea(H_SPACER));

        // Criando o logo
        Image feijoadaLogoIMG = UIUtils.resizeImage(Z808_FEIJOADA_LOGO_IMG_PATH, FEIJOADA_LOGO_SIZE_X, FEIJOADA_LOGO_SIZE_Y, Image.SCALE_SMOOTH);
        JLabel Z808_LOGO = new JLabel(new ImageIcon(feijoadaLogoIMG));
        Z808_LOGO.setPreferredSize(new Dimension(FEIJOADA_LOGO_SIZE_X, FEIJOADA_LOGO_SIZE_X));

        // Adicionando o logo
        upperTitle.add(Z808_LOGO);

        // Painel inferior com os botões
        JPanel lowerCommands = new JPanel();
        lowerCommands.setPreferredSize(new Dimension(0, 80));
        lowerCommands.setLayout(new BoxLayout(lowerCommands, BoxLayout.LINE_AXIS));

        // Criando o botão play
        JLabel playButton = new JLabel(PLAY_DEFAULT_IMG);
        playButton.setPreferredSize(new Dimension(CONTROLS_BUTTON_SIZE, CONTROLS_BUTTON_SIZE));

        // Botao play actions
        playButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseExited(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (playButton.getVisibleRect().contains(e.getPoint()))
                    mouseEntered(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                playButton.setIcon(PLAY_HOVER_IMG);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                playButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                playButton.setIcon(PLAY_DEFAULT_IMG);
            }
        });

        // Criando o botão step
        JLabel stepButton = new JLabel(STEP_DEFAULT_IMG);
        stepButton.setPreferredSize(new Dimension(CONTROLS_BUTTON_SIZE, CONTROLS_BUTTON_SIZE));

        // Botao step actions
        stepButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseExited(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (stepButton.getVisibleRect().contains(e.getPoint()))
                    mouseEntered(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
//                System.out.println(e.getX() + " " + e.getX());
//                System.out.println(stepButton.getVisibleRect());
                stepButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                stepButton.setIcon(STEP_ACTIVE_IMG);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                stepButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                stepButton.setIcon(STEP_DEFAULT_IMG);
            }
        });

        // Populando lowerCommands
        lowerCommands.add(Box.createRigidArea(H_SPACER));
        lowerCommands.add(playButton);
        lowerCommands.add(Box.createRigidArea(H_SPACER));
        lowerCommands.add(stepButton);

        // Criando painel central
        JPanel centralPannel = new JPanel();
        GroupLayout centralPanelLayout = new GroupLayout(centralPannel);
        centralPannel.setLayout(centralPanelLayout);
        centralPannel.setBackground(Color.black);

        // Criando text area
        JTextArea textArea = new JTextArea();
        textArea.setBounds(0, 0, 150, 150);

        centralPannel.add(textArea);

        // Adicionando os painéis
        z808UI.add(upperTitle, BorderLayout.NORTH);
        z808UI.add(lowerCommands, BorderLayout.SOUTH);
        z808UI.add(centralPannel, BorderLayout.CENTER);

        // Appear
        z808UI.setVisible(true);
    }
}
