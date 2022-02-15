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

	int[] get_next_position(int h,int m,int n,int ck,int cm,int cn){
		if(cn<n-1){
			return new int[]{ck,cm,cn+1};
		}
		else if(cm<m-1){
			return new int[]{ck,cm+1,0};
		}
		else if(ck < h-1){
			return new int[]{ck+1,0,0};
		}
		else{
			return new int[]{h,m,n};
		}
	}

	Boolean solve(ArrayList<Student>arr,int h,int m,int n,int currpos_k,int currpos_m,int currpos_n){

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

		if(currpos_k==h&&currpos_m==m&&currpos_n==n){
			return false;
		}

		
		int[] next_pos = get_next_position(h,m,n,currpos_k,currpos_m,currpos_n);

		ArrayList<String> visit = new ArrayList<>();
		for(Student s : arr){

			if(visit.contains(s.getBatch())){
				continue;
			}

			if(!state[currpos_k][currpos_m][currpos_n].filled && canbefilled(currpos_k,currpos_m,currpos_n,m,n,s.getBatch())){
				state[currpos_k][currpos_m][currpos_n].filled = true;
				Student nil_student = state[currpos_k][currpos_m][currpos_n].student;
				state[currpos_k][currpos_m][currpos_n].student = s;

				ArrayList<Student> arr_new = new ArrayList<>();
				arr_new.addAll(arr);
				arr_new.remove(s);
				
				if(!solve(arr_new,h,m,n,next_pos[0],next_pos[1],next_pos[2])){
					state[currpos_k][currpos_m][currpos_n].filled = false;
					state[currpos_k][currpos_m][currpos_n].student = nil_student;
					visit.add(s.getBatch());
				}
				else{
					return true;
				}
			}
		}
		return solve(arr,h,m,n,next_pos[0],next_pos[1],next_pos[2]);
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
		Boolean is_he_solved = solve(arr,k,m,n,0,0,0);
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