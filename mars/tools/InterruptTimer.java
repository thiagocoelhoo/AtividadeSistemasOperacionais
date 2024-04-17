package mars.tools;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

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

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

class IntegerFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {
        StringBuilder sb = new StringBuilder();
        sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.insert(offset, string);

        if (test(sb.toString())) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
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


public class InterruptTimer extends AbstractMarsToolAndApplication{
    private int amountOfInstructions;
    private JPanel panel;
    private JTextField textField;
    private JButton startButton;
    private JButton resetButton;
    private JButton helpButton;
    private JLabel label;

    @Override
    public String getName() {
        return "Interrupt Timer";
    }

    @Override
    public void action() {
        JDialog dialog = new JDialog((JFrame) null, getName(), true); 
        dialog.getContentPane().add(buildMainDisplayArea()); 
        dialog.pack();
        dialog.setVisible(true);
}

    public InterruptTimer(){
        super("Interrupt Timer", "Interrupt Timer");
        this.amountOfInstructions = 0;
    }

    public InterruptTimer(int amountOfInstructions){
        super("Interrupt Timer", "Interrupt Timer");
        this.amountOfInstructions = amountOfInstructions;
    }

    protected JComponent buildMainDisplayArea() {
        Box box = Box.createVerticalBox();
        box.add(buildDisplayArea());
        return box;
     }

    private JPanel buildDisplayArea() {
        panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        
        label = new JLabel("Amount of Instructions: " + amountOfInstructions);
        textField = new JTextField(10);
        startButton = new JButton("Start");
        resetButton = new JButton("Reset");
        helpButton = new JButton("Help");
    
        JPanel topPanel = new JPanel();
        topPanel.add(label);
        Document doc = textField.getDocument();
        if (doc instanceof AbstractDocument) {
            ((AbstractDocument) doc).setDocumentFilter(new IntegerFilter());
        }

        JPanel middlePanel = new JPanel();
        middlePanel.add(textField);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(startButton);
        bottomPanel.add(resetButton);
        bottomPanel.add(helpButton);

        startButton.addActionListener(e -> {
            System.out.println("Botão Start pressionado!");
        });

        resetButton.addActionListener(e -> {
            System.out.println("Botão Reset pressionado!");
        });

        helpButton.addActionListener(e -> {
            System.out.println("Botão Help pressionado!");
        });
    
        panel.add(topPanel);
        panel.add(middlePanel);
        panel.add(bottomPanel);
    
        panel.setVisible(true);
        return panel;
    }
    
}
