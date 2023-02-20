package br.com.Yago.calc.visao;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.Yago.calc.modelo.Memoria;
import br.com.Yago.calc.modelo.MemoriaObservador;

@SuppressWarnings("serial")
public class Display extends JPanel implements MemoriaObservador{
	
	private final JLabel label ;
	
	public Display() {
		Memoria.getInstancia().adicionarObservador(this);
		setBackground(new Color(46,49,50));// Cor do display
		label = new JLabel(Memoria.getInstancia().getTextoAtual());
		label.setForeground(Color.white);//Cor da fonte
		label.setFont(new Font("courier", Font.PLAIN,30));
		setLayout(new FlowLayout(FlowLayout.RIGHT,10,25));// inicia o texto na direita e na parte de baixo do display
		add(label);
	}
   @Override
	public void valorAlterado(String novoValor) {
	label.setText(novoValor);
}
}
