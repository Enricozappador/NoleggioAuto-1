package polito.it.noleggio.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.PriorityQueue;

import polito.it.noleggio.model.Event.EventType;

public class Simulator {
	
	private PriorityQueue<Event> queue = new PriorityQueue<>(); 
	
	private int NC = 10; 
	private Duration T_IN = Duration.of(10, ChronoUnit.MINUTES); 
	
	private final LocalTime oraApertura =LocalTime.of(8, 00);
	private final LocalTime oraChiusura =LocalTime.of(17, 00);
	
	
	private int nAuto; 
	
	
	private int clienti; 
	private int insoddisfatti; 
	
	public void setNumCars(int N) {
		this.NC = N; 
	}
	
	public void setClientFrequency(Duration d) {
		this.T_IN = d; 
	}

	public int getClienti() {
		return clienti;
	}

	public int getInsoddisfatti() {
		return insoddisfatti;
	}
	
	public void run() {
		this.nAuto = this.NC; 
		this.clienti = this.insoddisfatti = 0; 
		
		this.queue.clear();
		LocalTime oraArrivoCliente= this.oraApertura; 
		do {
			
			Event e = new Event(oraArrivoCliente, EventType.NEW_CLIENT);
			this.queue.add(e); 
			oraArrivoCliente = oraArrivoCliente.plus(this.T_IN);
			
		}while(oraArrivoCliente.isBefore(this.oraChiusura)); 
		
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll(); 
			System.out.println(e);
			processEvent(e); 
			
			
		}
			
			
			
			
		
		
	}
	
	private void processEvent(Event e) {
		switch(e.getType()) {
			case NEW_CLIENT:
				
				if(this.nAuto>0) {
					this.nAuto--; 
					this.clienti++; 
					double num = Math.random();
					Duration travel; 
					if(num<1.0/3.0)
						travel = Duration.of(1,ChronoUnit.HOURS);
					else if(num<2.0/3.0)
						travel = Duration.of(2,ChronoUnit.HOURS);
					else 
						travel = Duration.of(3,ChronoUnit.HOURS);
					
					Event nuovo = new Event(e.getTime().plus(travel), EventType.CAR_RETURNED);
					this.queue.add(nuovo);
				}else {
					this.insoddisfatti++; 
					this.clienti++; 
					
				}
				break; 
				
			case CAR_RETURNED: 
				
				this.nAuto++;
				
				break; 
			}
	}

	public int getTotClients() {
				return this.clienti;
	}

	public int getDissatisfied() {
				return this.insoddisfatti;
	}

	
	

}
