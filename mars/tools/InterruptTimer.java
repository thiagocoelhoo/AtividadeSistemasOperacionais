package mars.tools;
// Descrição da Implementação:  Escalonamento preemptivo

// PARTE 1 – ESCALONAMENTO PREEMPTIVO

// •	Crie uma uma nova ferramenta (tool) do MARS que funcionará como um timer

// •	A criação de ferramenta deve seguir o tutorial disponível em http://courses.missouristate.edu/KenVollmar/mars/tutorial.htm
// •	Há duas formas de criar ferramentas:
// o	Implementando a interface mars.tools.MarsTool
// o	Ou extendendo a classe abstrata mars.tools.AbstractMarsToolAndApplication
// •	A ferramenta deve permitir configurar de quanto em quanto tempo o timer deve gerar interrupção, um botão de iniciar a contagem do timer e os botões comuns a todas as ferramentas do MARS (conectar ao MIPS, reset, ajuda e fechar)
// •	O tempo do timer deve ser definido em quantidade de instruções executadas
// o	Essa configuração (quantidade de instruções a ser contada) deve está disponível na interface gráfica para o usuário
// o	Uma opção para contar a quantidade de instruções executadas é sua ferramenta adicionar a memória como “observável”. E sempre que um endereço de memória (dentro do limite do segmento de código) for acessado a ferramenta seria notificada para contar como uma instrução executada.
// o	Dica: vejam o código da ferramenta “InstructionCounter” como inspiração
// •	Quando o tempo configurado do timer for atingido acontece uma “interrupção”, o contador do timer zerado e uma nova contagem iniciada.
// o	O tratamento dessa “interrupção” deve corresponder a troca de processos, ou seja, o algoritmo de escalonamento é invocado.
// o	Esse mecanismo de interrupção deve substituir a syscall SycallProcessChange. Assim, quando a interrupção do timer ocorrer o contexto do processo em execução deve ser salvo em sua PCB, colocando-o no estado “Pronto” e o algoritmo de escalonamento deve escolher outro processo “Pronto” para executar. Depois de escolhido, o processo é colocado no estado “Executando”, seu contexto é carregado de sua PCB para o processador, do mesmo modo quando SycallProcessChange era chamada. 
// o	A implementação dos processos não deve realizar a chamada de sistema SycallProcessChange nos testes, mesmo essa syscall continuando disponível
// •	Para criação de processos deve ser usada a mesma chamada de sistema (SyscallFork) e podem incluir alguma mudança se necessário.
// •	Teste a nova funcionalidade com o seguinte código

// MyTool implements MarsTool  approach

// Extract the MARS distribution from its JAR file.  The JAR file does not have an outermost folder to contain everything, so you'll want to create one and extract it into that folder.

// Develop your class in the mars.tools package (mars/tools folder).

// Your class must implement the MarsTool interface, which is in that package.  This has only two methods: String getName() to return the name to be displayed in its Tools menu item, and void action() which is invoked when that menu item is selected by the MARS user.  These will assure its inclusion in the Tools menu when MARS is launched.

// The user interface should be based on the javax.swing.JDialog class.  The tool interacts with simulated MIPS memory and registers through the mars.mips.hardware.Memory and mars.mips.hardware.Register classes, both of which extend java.util.Observable.  The Memory class provides several addObserver() methods that permit an Observer to register for selected memory addresses or ranges.  Javadoc-produced documentation is available in the doc folder of the MARS distribution.  

// After successful compilation, MARS will automatically include the new tool in its Tools menu.

// Extract the MARS distribution from its JAR file if you have not already done so.

// Develop your class in the mars.tools package (mars/tools folder).

// Your class must extend the AbstractMarsToolAndApplication abstract class, which is in that package.  Nineteen of the 21 methods in this class have default implementations.  

// Define at least the two abstract methods: String getName() to return the tool’s display name, and JComponent buildMainDisplayArea() to construct the central area of the tool’s graphical user interface.  It will automatically be placed in the CENTER of a BorderLayout, with title information to its NORTH and tool control buttons to its SOUTH.  Several addAsObserver() methods are available for registering as a memory and/or register observer.

// Override additional methods as desired.  Some do nothing by default. 

// After successful compilation, MARS will automatically include the new tool in its Tools menu.

// To run it as a stand-alone application, you either need to add a main() to create the tool object and call its go() method or write a short external application to do the same.


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.NumberFormatter;

import mars.ProgramStatement;
import mars.mips.SO.ProcessManager.Scheduler;
import mars.mips.hardware.AccessNotice;
import mars.mips.hardware.Memory;
import mars.simulator.Simulator;

class IntegerFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        StringBuilder sb = new StringBuilder();
        sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.insert(offset, string);

        if (test(sb.toString())) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        StringBuilder sb = new StringBuilder();
        sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.replace(offset, offset + length, text);

        if (test(sb.toString())) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    private boolean test(String text) {
        if (text.isEmpty()) {
            return true;
        }

        try {
            int value = Integer.parseInt(text);
            return value >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

// TODO: Implementar funcionalidade da Tool 
public class InterruptTimer extends AbstractMarsToolAndApplication {
    private static String name = "Interrupt Timer";
    private static String version = "Version 1.0";
    private static String heading = "Interrupt Timer";

    private int counter;
    private int amountOfInstructions;
    private String algorithm;

    private JPanel panel;
    private JTextField textField;
    private JLabel label;

    public InterruptTimer() {
        super(name, heading);
        this.amountOfInstructions = 0;
        this.counter = 0;
        this.algorithm = "FIFO";
    }

    public InterruptTimer(int amountOfInstructions) {
        super(name, heading);
        this.amountOfInstructions = amountOfInstructions;
        this.counter = 0;
        this.algorithm = "FIFO";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    protected void addAsObserver() {
        addAsObserver(Memory.textBaseAddress, Memory.textLimitAddress);
    }

    @Override
    protected void processMIPSUpdate(Observable resource, AccessNotice notice) {
        if (!notice.accessIsFromMIPS()) {
            return;
        }

        if (notice.getAccessType() != AccessNotice.READ) {
            return;
        }
        counter++;

        // Quando o contador atingir a quantidade de instruções configurada, o escalonador é chamado
        if (counter > 0 && amountOfInstructions != 0 && counter == amountOfInstructions) {
            Scheduler.schedule();
            counter = 0;
        }

        updateDisplay();
    }

    @Override
    protected void initializePreGUI() {
        // counter = counterR = counterI = counterJ = 0;
        // lastAddress = -1;
    }

    @Override
    protected void reset() {
        counter = 0;
        updateDisplay();
    }

    protected JComponent getHelpComponent() {
        final String helpContent = 
        	" This tool is composed of 3 parts : two seven-segment displays, an hexadecimal keyboard and counter \n"+
        	"Seven segment display\n"+
        	" Byte value at address 0xFFFF0010 : command right seven segment display \n "+
        	" Byte value at address 0xFFFF0011 : command left seven segment display \n "+
        	" Each bit of these two bytes are connected to segments (bit 0 for a segment, 1 for b segment and 7 for point \n \n"+
        	"Hexadecimal keyboard\n"+
        	" Byte value at address 0xFFFF0012 : command row number of hexadecimal keyboard (bit 0 to 3) and enable keyboard interrupt (bit 7) \n" +
        	" Byte value at address 0xFFFF0014 : receive row and column of the key pressed, 0 if not key pressed \n"+
        	" The mips program have to scan, one by one, each row (send 1,2,4,8...)"+
        	" and then observe if a key is pressed (that mean byte value at adresse 0xFFFF0014 is different from zero). "+
        	" This byte value is composed of row number (4 left bits) and column number (4 right bits)"+
        	" Here you'll find the code for each key : 0x11,0x21,0x41,0x81,0x12,0x22,0x42,0x82,0x14,0x24,0x44,0x84,0x18,0x28,0x48,0x88. \n"+
        	" For exemple key number 2 return 0x41, that mean the key is on column 3 and row 1. \n"+
        	" If keyboard interruption is enable, an exception is started, with cause register bit number 11 set.\n \n"+
        	"Counter\n"+
        	" Byte value at address 0xFFFF0013 : If one bit of this byte is set, the counter interruption is enable.\n"+
        	" If counter interruption is enable, every 30 instructions, an exception is started with cause register bit number 10.\n" +
			"   (contributed by Didier Teifreto, dteifreto@lifc.univ-fcomte.fr)"
        	;
        JButton help = new JButton("Help");
        help.addActionListener(
        		new ActionListener() {
        			public void actionPerformed(ActionEvent e) {
        				JTextArea ja = new JTextArea(helpContent);
        				ja.setRows(20);
        				ja.setColumns(60);
        				ja.setLineWrap(true);
        				ja.setWrapStyleWord(true);
        				JOptionPane.showMessageDialog(theWindow, new JScrollPane(ja),
                        "Interrupt Timer Tools", JOptionPane.INFORMATION_MESSAGE);
        			}
        		});		
        return help;  
    }

    protected JComponent buildMainDisplayArea() {
        panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JPanel topPanel = new JPanel();
        label = new JLabel("Amount of Instructions: " + amountOfInstructions);
        topPanel.add(label);

        JPanel middlePanel = new JPanel();
        textField = new JTextField(10);

        // Modificar o valor da variável de amountOfInstructions sempre que o usuário digitar um novo valor
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (textField.getText().isEmpty()) {
                    return;
                }
                amountOfInstructions = Integer.parseInt(textField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (textField.getText().isEmpty()) {
                    return;
                }
                amountOfInstructions = Integer.parseInt(textField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (textField.getText().isEmpty()) {
                    return;
                }
                amountOfInstructions = Integer.parseInt(textField.getText());
            }
        });

        middlePanel.add(textField);
        
        Document doc = textField.getDocument();
        if (doc instanceof AbstractDocument) {
            ((AbstractDocument) doc).setDocumentFilter(new IntegerFilter());
        }

        // Combobox para selecionar o tipo de algoritmo de escalonamento, por prioridade fixa, FIFO ou por loteria
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("FIFO");
        comboBox.addItem("Lottery");
        comboBox.addItem("Fixed Priority");

        // Quando for alterado 
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                @SuppressWarnings("rawtypes")
                JComboBox cb = (JComboBox) e.getSource();
                algorithm = (String) cb.getSelectedItem();    
            
                if (algorithm.equals("FIFO")) {
                    Scheduler.setAlgorithm(Scheduler.ALGORITHM.FIFO);
                } else if (algorithm.equals("Lottery")) {
                    Scheduler.setAlgorithm(Scheduler.ALGORITHM.LOTTERY);
                } else if (algorithm.equals("Fixed Priority")) {
                    Scheduler.setAlgorithm(Scheduler.ALGORITHM.FIXED_PRIORITY);
                }
                
            }
        });

        middlePanel.add(comboBox);


        panel.add(topPanel);
        panel.add(middlePanel);

        return panel;
    }


    // @Override
    protected void updateDisplay() {
        label.setText("Amount of Instructions: " + counter);
    }

}
