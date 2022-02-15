import java.util.*;
import java.io.*;
import java.util.Scanner;
import java.util.Collection;

class Student{
	private String batch;
	private String roll;
	Student(String batch,String roll){
		this.batch = batch;
		this.roll = roll;
	}
	public String getBatch(){
		return batch;
	}
	public String getRoll(){
		return roll;
	}
}

class Position{
	public int[] coor;
	public int minimum_remaining;
	Position(int[] coor,int minimum_remaining){
		this.coor = coor;
		this.minimum_remaining = minimum_remaining;
	}
}

class Solver{

	Cell[][][] state;

	ArrayList<String>get_neighbours_batch(int i,int j,int k,int m,int n){
		ArrayList<String> arr = new ArrayList<>();
		int[][] neighbour_opration = {{-1,-1},{-1,0},{-1,1},{0,1},{1,1},{1,0},{1,-1},{0,-1}};
		for(int[] opration : neighbour_opration){
			if(j+opration[0]<m && j+opration[0] >=0 && k+opration[1]<n && k+opration[1] >=0){
				if(state[i][j+opration[0]][k+opration[1]].filled){
					arr.add(state[i][j+opration[0]][k+opration[1]].student.getBatch());
				}
			}
		}
		return arr;
	}

	Boolean canbefilled(int i,int j,int k,int m,int n,String s){
		for(String filled_batch : get_neighbours_batch(i,j,k,m,n)){
			if(filled_batch.equals(s)){
				return false;
			}
		}
		return true;
	}

	ArrayList<Position> getPosition(ArrayList<Student>arr,int h,int m,int n){
		ArrayList<Position> temp_arr = new ArrayList<>();

		for(int i=0;i<h;i++){
			for(int j=0;j<m;j++){
				for(int k=0;k<n;k++){
					if(!state[i][j][k].filled){
						int n_student = 0;
						for(Student s : arr){
							if(canbefilled(i,j,k,m,n,s.getBatch())){
								n_student++;
							}
						}
						temp_arr.add(new Position(new int[] {i,j,k}, n_student));
					}
				}
			}
		}
		Comparator<Position> mrvsort = new Comparator<Position>()
		{
			public int compare(Position a,Position b)
			{
				return a.minimum_remaining - b.minimum_remaining;
			}
		};
		Collections.sort(temp_arr,mrvsort);
		return temp_arr; 
	}

	Boolean solve(ArrayList<Student>arr,int h,int m,int n){

		if(arr.size()==0){
			for(int i =0;i<h;i++){
				for(int j=0;j<m;j++){
					for(int k=0;k<n-1;k++){
						System.out.print(state[i][j][k].student.getBatch() + " "+state[i][j][k].student.getRoll()+ " ");
					}
					System.out.println(state[i][j][n-1].student.getBatch() + " "+state[i][j][n-1].student.getRoll()+ " ");
				}
			}
			return true;
		}
		

		ArrayList<Position> position = getPosition(arr,h,m,n);
		//System.out.println(position);
		//System.out.println(arr);
		for(Position pos : position){
			int currpos_k = pos.coor[0];
			int currpos_m = pos.coor[1];
			int currpos_n = pos.coor[2];
			ArrayList<String> visit = new ArrayList<>();
			if(!state[currpos_k][currpos_m][currpos_n].filled){
				for(Student s : arr){

					if(visit.contains(s.getBatch())){
						continue;
					}

					if(canbefilled(currpos_k,currpos_m,currpos_n,m,n,s.getBatch())){
						state[currpos_k][currpos_m][currpos_n].filled = true;
						Student nil_student = state[currpos_k][currpos_m][currpos_n].student;
						state[currpos_k][currpos_m][currpos_n].student = s;

						ArrayList<Student> arr_new = new ArrayList<>();
						arr_new.addAll(arr);
						arr_new.remove(s);
						if(!solve(arr_new,h,m,n)){
							state[currpos_k][currpos_m][currpos_n].filled = false;
							state[currpos_k][currpos_m][currpos_n].student = nil_student;
							visit.add(s.getBatch());
						}
						else{
							return true;
						}
					}
				}
			}
		}

		return false;
	}
	void start(ArrayList<Student> arr,int k,int m,int n){

		if(arr.size()>k*m*n){
			System.out.println("-1");
			return;
		}

		Comparator<Student> batchsort = new Comparator<Student>()
		{
			public int compare(Student a,Student b)
			{
				return (a.getBatch()).compareTo(b.getBatch());
			}
		 
		};

		Comparator<Student> rollsort = new Comparator<Student>()
		{
			public int compare(Student a,Student b)
			{
				return a.getRoll().compareTo(b.getRoll());
			}
		 
		};

		state = new Cell[k][m][n];
		for(int i =0;i<k;i++){
			for(int j=0;j<m;j++){
				for(int kk=0;kk<n;kk++){
					state[i][j][kk] = new Cell();
				}
			}
		}
		Collections.sort(arr,batchsort.thenComparing(rollsort));
		Boolean is_he_solved = solve(arr,k,m,n);
		if(!is_he_solved){
			System.out.println("-1");
			return;
		}
	}
}

class Cell{
	Boolean filled;
	Student student;
	Cell(){
		this.filled = false;
		this.student = new Student("NIL","NIL");
	}
}

class test{
	public static void main(String[] args){
		Scanner scanner  = new Scanner(System.in);
		int t = scanner.nextInt();
		Solver agent = new Solver();
		while(t>0){
			int k= scanner.nextInt();
			int m= scanner.nextInt();
			int n= scanner.nextInt();
			int l = scanner.nextInt();
			ArrayList<Student>arr = new ArrayList<>();
			for(int j=0;j<l;j+=1){
				String batch = scanner.next();
				String roll = scanner.next();
				arr.add(new Student(batch,roll));
			}
			agent.start(arr,k,m,n);
			t-=1;
		}
	}
}