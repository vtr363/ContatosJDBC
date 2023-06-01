package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dao.ContatoDao;
import model.Contato;

public class Controller {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		boolean running = true;
		while(running) {
			
			System.out.println("+-----------------+\n"
					+ "|Lista de contatos|\n"
					+ "+-----------------+\n"
					+ "\t Opções\n\n"
					+ "1) Listar todos os contatos\n"
					+ "2) Listar contatos por letra inicial\n"
					+ "3) Adcionar novo contato\n"
					+ "4) Alterar contato\n"
					+ "5) Remover contato\n"
					+ "6) Sair\n\n"
					+ "Digite a opção desejada: ");
			String input = scanner.nextLine();
			
			switch (input) {
			case "1":{
				listarTodos();
				break;
			}
			case "2":{
				listarPorLetraInicial(scanner);
				break;
			}
			case "3":{
				adicionarContato(scanner);
				break;
			}
			case "4":{
				alterarContato(scanner);
				break;
			}
			case "5":{
				deletarContato(scanner);
				break;
			}
			case "6": {
				running = false;
				scanner.close();
				System.out.println("Finalizando!");
				break;
			}
			default:
				System.out.println("Opção invalida, tente novamente");
			}
		}
	}
	
	
	private static void deletarContato(Scanner scanner) {
		
		boolean listaContatos = listarTodos();
		
		if(listaContatos) {
			try{
				ContatoDao dao = new ContatoDao();
				System.out.println("Digite o id do contato que deseja deletar: ");
				Long contatoId = scanner.nextLong();
				if (!dao.deleteContato(contatoId)) {
					System.out.println("Contato não encontrado!");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (NumberFormatException nfe) {
				System.out.println("Valor Invalido, digite apenas numeros.");
			}
		}
	}


	static boolean listarTodos() {
		List<Contato> listaContatos = new ArrayList<>();
		try {
			ContatoDao dao = new ContatoDao();
			listaContatos = dao.getAllContatos();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		if(listaContatos.isEmpty()) {
			System.out.println("Você ainda não possui contatos!");
			return false;
		}else {
			mostraContatos(listaContatos);
		}
		return true;
	}
	
	
	static void listarPorLetraInicial(Scanner scanner) {
		List<Contato> listaContatos = new ArrayList<>();
		try {
			ContatoDao dao = new ContatoDao();
			System.out.println("digite a letra inicial ou nome do contato: ");
			listaContatos = dao.getContatosByFirstLetter(scanner.nextLine());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		if(listaContatos.isEmpty()) {
			System.out.println("Nenhum contato encontrado");
			
		}else {
			mostraContatos(listaContatos);
		}
		
	}
	
	
	static void adicionarContato(Scanner scanner) {
		Contato contato = new Contato();
		String nome;
		String email;
		String endereco;
		
		
		System.out.println("Nome: ");
		nome = scanner.nextLine();
		
		System.out.println("Email: ");
		email = scanner.nextLine();
		
		System.out.println("Endereço: ");
		endereco = scanner.nextLine();
		
		if (nome.isBlank() || email.isBlank() || endereco.isBlank()){
			System.out.println("Não são permitidos campos vazios.\n"
							 + "Retornando ao menu inicial.");
			return;
		}
			
		
		
		contato.setNome(nome);
	
		contato.setEmail(email);
	
		contato.setEndereco(endereco);
		
		
		ContatoDao dao;
		try {
			dao = new ContatoDao();
			dao.addContato(contato);
			System.out.println("Contato adicionado com sucesso!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	static void alterarContato(Scanner scanner) {
		Contato contato = new Contato();
		
		boolean listaContatos =  listarTodos();
		
		if(listaContatos) {
			boolean tentativa = true;
			do {
				System.out.println("Digite o Id do contato que deseja alterar: ");
				try {
					int idContato = scanner.nextInt();
					scanner.nextLine();
					tentativa = false;
					ContatoDao dao = new ContatoDao();
					contato = dao.getContatoById(idContato);
					Boolean alterado = false;
					
					System.out.println("deixe em branco caso não queira alterar alguma informação");
					System.out.println("Nome: ");
					String nome = scanner.nextLine();
					
					System.out.println("Email: ");
					String email = scanner.nextLine();
					
					System.out.println("Endereço: ");
					String endereco = scanner.nextLine();
					
					if(!nome.isBlank()) {
						contato.setNome(nome);
						alterado = true;
					}
					if(!email.isBlank()) {
						contato.setEmail(email);
						alterado = true;
					}
					if(!endereco.isBlank()) {
						contato.setEndereco(endereco);
						alterado = true;
					}
					
					if(alterado) {
						dao = new ContatoDao();
						System.out.println(contato.getId());
						dao.updateContato(contato);
						System.out.println("Contato alterado com sucesso");
					}
					
					
					
				}catch (NumberFormatException nfe) {
					System.out.println("Valor Invalido, digite apenas numeros."
									 + "\nGostaria de tentar novamente? (S/n)");
					
					if(scanner.nextLine().toLowerCase() != "s") {
						tentativa = false;
						System.out.println("Voltando ao menu principal.");
					}
					
				} catch (SQLException e) {
					System.out.println("erro ao alterar o contato.");
					e.printStackTrace();
				}
			}while(tentativa);
			
			
			
			
		}
		
	}
	
	static void mostraContatos(List<Contato> listaContatos) {
		
		for (Contato contato : listaContatos) {
			System.out.println("\n--------------------------"
							 + "\nId: " + contato.getId()
							 + "\nNome: " + contato.getNome()
							 + "\nEndereço: " + contato.getEndereco()
							 + "\nEmail: " + contato.getEmail()
							 + "\n--------------------------");
		
		}
	}
	
}
