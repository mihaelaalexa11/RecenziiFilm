import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RecenziiFilm {
	
	private JFrame fereastra;
	private JTextArea afisareRecenzii;
	private JTextField titluF, anF, notaF, dataF, recenzieF, cautareF;
	private List<Recenzie> recenzii;
	private final String FISIER = "recenzii.txt";
	
	public RecenziiFilm() {
		
		recenzii = new ArrayList<>();
		incarcaRecenziiDinFisier();
		initializazaInterfata();
	}
	
	private void initializazaInterfata() {
		
		fereastra = new JFrame("Recenzii filme");
		fereastra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fereastra.setSize(900, 600);
		fereastra.setLayout(new BorderLayout());
		
		JPanel panouInput = new JPanel(new GridLayout(6, 2));
		titluF = new JTextField();
		anF = new JTextField(); 
		notaF = new JTextField(); 
		dataF = new JTextField(); 
		recenzieF = new JTextField(); 
		cautareF = new JTextField();
		
		panouInput.add(new JLabel("Titlu"));
		panouInput.add(titluF);
	    panouInput.add(new JLabel("An:"));
	    panouInput.add(anF);
	    panouInput.add(new JLabel("Nota:"));
	    panouInput.add(notaF);
	    panouInput.add(new JLabel("Data:"));
	    panouInput.add(dataF);
	    panouInput.add(new JLabel("Recenzie:"));
	    panouInput.add(recenzieF);
	    panouInput.add(new JLabel("Căutare:"));
	    panouInput.add(cautareF);
	    
	    JButton butonAdaugare = new JButton("Adauga recenzie");
	    JButton butonAfisare = new JButton("Afiseaza recenzii");
	    JButton butonSortare = new JButton("Sorteaza recenziile");
	    JButton butonCautare = new JButton("Cauta recenzii");
	    JButton butonEditare = new JButton("Editeaza recenzie");
	    JButton butonDetalii = new JButton("Detalii recenzie");
	    
	    butonAdaugare.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		adaugaRecenzie();
	    		
	    	}
	    });
	    
	    butonAfisare.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            afiseazaRecenzii();
	        }
	    });
	    
	    butonSortare.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            sorteazaRecenziile();
	        }
	    });
	    
	    butonCautare.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            cautaRecenzii();
	        }
	    });
	    
	    butonEditare.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            editeazaRecenzie();
	        }
	    });
	    
	    butonDetalii.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            detaliiRecenzie();
	        }
	    });
	    
	    
	    JPanel panouButoane = new JPanel();
	    panouButoane.add(butonAdaugare);
	    panouButoane.add(butonAfisare);
	    panouButoane.add(butonSortare);
	    panouButoane.add(butonCautare);
	    panouButoane.add(butonEditare);
	    panouButoane.add(butonDetalii);
	    
	    afisareRecenzii = new JTextArea();
	    afisareRecenzii.setEditable(false);
	    
	    fereastra.add(panouInput, BorderLayout.NORTH);
	    fereastra.add(new JScrollPane(afisareRecenzii), BorderLayout.CENTER);
	    fereastra.add(panouButoane, BorderLayout.SOUTH);
	    
	    fereastra.setVisible(true);        
	}
	
	private void adaugaRecenzie() {
		
	    try {
	        String titlu = titluF.getText();
	        String recenzie = recenzieF.getText();
	        String data = dataF.getText();
	        String anText = anF.getText();		
	        int an = anText.isEmpty() ? 0 : Integer.parseInt(anText);		//conversia textului in numar intreg
	        String notaText = notaF.getText();
	        double nota = notaText.isEmpty() ? 0.0 : Double.parseDouble(notaText); 		//conversia textului in numar real
	        
	        Recenzie r = new Recenzie(titlu, an, nota, recenzie, data);
	        recenzii.add(r);
	        salveazaRecenziiInFisier();
	        sorteazaRecenziile();

	        titluF.setText("");
	        anF.setText("");
	        notaF.setText("");
	        dataF.setText("");
	        recenzieF.setText("");

	        JOptionPane.showMessageDialog(fereastra, "Recenzia a fost adăugată cu succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);

	    } catch (NumberFormatException ex) {
	        JOptionPane.showMessageDialog(fereastra, "Date invalide!", "Eroare", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	private void afiseazaRecenzii() {
		
		afisareRecenzii.setText("");
		for (int i = 0; i < recenzii.size(); i++) {
            afisareRecenzii.append((i + 1) + ". " + recenzii.get(i).getTitlu() + "\n");
		}
	}
	
	private void sorteazaRecenziile() {

	    for (int i = 0; i < recenzii.size() - 1; i++) {
	        for (int j = 0; j < recenzii.size() - 1 - i; j++) {
	        	if (Double.compare(recenzii.get(j).getNota(), recenzii.get(j + 1).getNota()) < 0) {
	                Recenzie temp = recenzii.get(j);
	                recenzii.set(j, recenzii.get(j + 1));
	                recenzii.set(j + 1, temp);
	            }
	        }
	    }
	    
	    afiseazaRecenzii();
	}
	
	private void cautaRecenzii() {
		
		String cuvCheie = cautareF.getText().toLowerCase();
		afisareRecenzii.setText("");
		
		boolean gasit = false;
		
		for (Recenzie r : recenzii) {
			if(r.getTitlu().toLowerCase().contains(cuvCheie) || r.getTextRecenzie().toLowerCase().contains(cuvCheie)) {
				afisareRecenzii.append(r.toString() + "\n");
				gasit = true;
			}
		}
		
		if(!gasit) {
			afisareRecenzii.append("Nu au fost gassite recenzii in care sa se gaseasza cuvantul: " + cuvCheie + "\n");
		}
	}
	
	private void editeazaRecenzie() {
		
		String titluSelectat = JOptionPane.showInputDialog(fereastra, "Introdu titlul filmului: ");
		for (Recenzie r : recenzii) {
			if(r.getTitlu().equals(titluSelectat)){
				titluF.setText(r.getTitlu());
				anF.setText(String.valueOf(r.getAn()));
	            notaF.setText(String.valueOf(r.getNota()));
	            dataF.setText(r.getData());
	            recenzieF.setText(r.getTextRecenzie());
	            
	            r.setTitlu(titluF.getText());
	            r.setAn(Integer.parseInt(anF.getText()));
	            r.setNota(Double.parseDouble(notaF.getText()));
	            r.setData(dataF.getText());
	            r.setTextRecenzie(recenzieF.getText());

	            salveazaRecenziiInFisier();
	            JOptionPane.showMessageDialog(fereastra, "Recenzia a fost modificată cu succes!", "Succes", JOptionPane.INFORMATION_MESSAGE); 
	             return;	
			}
		} 
		JOptionPane.showMessageDialog(fereastra, "Film negăsit!", "Eroare", JOptionPane.ERROR_MESSAGE);
	}
	
	private void salveazaRecenziiInFisier() {
		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(FISIER))){
			for (Recenzie r : recenzii) {
				writer.write(r.getTitlu() + "," + r.getAn() + "," + r.getNota() + "," + r.getData() + "," + r.getTextRecenzie());
				writer.newLine();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void incarcaRecenziiDinFisier() {
		
	    try (BufferedReader reader = new BufferedReader(new FileReader(FISIER))) {
	        String linie;
	        while ((linie = reader.readLine()) != null) {
	            String[] campuri = linie.split(",");
	            if (campuri.length == 5) { 
	                String titlu = campuri[0];
	                int an = Integer.parseInt(campuri[1]);
	                double nota = Double.parseDouble(campuri[2]);
	                String data = campuri[3];
	                String recenzie = campuri[4];

	                Recenzie r = new Recenzie(titlu, an, nota, recenzie, data);
	                recenzii.add(r);
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private void detaliiRecenzie() {
		
		String input = JOptionPane.showInputDialog(fereastra, "Introdu numărul recenziei:");
        try {
            int index = Integer.parseInt(input) - 1;
            if (index >= 0 && index < recenzii.size()) {
                Recenzie r = recenzii.get(index);
                JOptionPane.showMessageDialog(fereastra, r.toString(), "Detalii Recenzie", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(fereastra, "Număr invalid!", "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(fereastra, "Introduceți un număr valid!", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
	}
	
	 public static void main(String[] args) {
	        new RecenziiFilm();
	    }
	}

	class Recenzie implements Serializable {
		
		private String titlu;
		private int an;
		private double nota;
		private String textRecenzie;
		private String data;
		
		public Recenzie(String titlu, int an, double nota, String textRecenzie, String data) {
			this.titlu = titlu;
			this.an = an;
			this.nota = nota;
			this.textRecenzie = textRecenzie;
			this.data = data;
		}
		
		public double getNota() {
		    return nota;
		}
		
		public String getTitlu() {
			return titlu;
		}
		
		public String getTextRecenzie() {
			return textRecenzie;
		}
		
		public int getAn() {
		    return an;
		}
		
		public String getData() {
		    return data;
		}
		
		public void setTitlu(String titlu) {
		    this.titlu = titlu;
		}

		public void setAn(int an) {
		    this.an = an;
		}

		public void setNota(double nota) {
		    this.nota = nota;
		}

		public void setTextRecenzie(String textRecenzie) {
		    this.textRecenzie = textRecenzie;
		}

		public void setData(String data) {
		    this.data = data;
		}
		
		public String toString() {
			return "Titlu: " + titlu + ", An: " + an + ", Nota: " + nota + ", Data: " + data + ", Recenzie " + textRecenzie;	
		}
}
