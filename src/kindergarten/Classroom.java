package kindergarten;
/**
 * This class represents a Classroom, with:
 * - an SNode instance variable for students in line,
 * - an SNode instance variable for musical chairs, pointing to the last student in the list,
 * - a boolean array for seating availability (eg. can a student sit in a given seat), and
 * - a Student array parallel to seatingAvailability to show students filed into seats 
 * --- (more formally, seatingAvailability[i][j] also refers to the same seat in studentsSitting[i][j])
 * 
 * @author Ethan Chou
 * @author Kal Pandit
 * @author Maksims Kurjanovics Kravcenko
 */
public class Classroom {
    private SNode studentsInLine;             // when students are in line: references the FIRST student in the LL
    private SNode musicalChairs;              // when students are in musical chairs: references the LAST student in the CLL
    private boolean[][] seatingAvailability;  // represents the classroom seats that are available to students
    private Student[][] studentsSitting;      // when students are sitting in the classroom: contains the students

    /**
     * Constructor for classrooms. Do not edit.
     * @param l passes in students in line
     * @param m passes in musical chairs
     * @param a passes in availability
     * @param s passes in students sitting
     */
    public Classroom ( SNode l, SNode m, boolean[][] a, Student[][] s ) {
		studentsInLine      = l;
        musicalChairs       = m;
		seatingAvailability = a;
        studentsSitting     = s;
	}
    /**
     * Default constructor starts an empty classroom. Do not edit.
     */
    public Classroom() {
        this(null, null, null, null);
    }

    /**
     * This method simulates students coming into the classroom and standing in line.
     * 
     * Reads students from input file and inserts these students in alphabetical 
     * order to studentsInLine singly linked list.
     * 
     * Input file has:
     * 1) one line containing an integer representing the number of students in the file, say x
     * 2) x lines containing one student per line. Each line has the following student 
     * information separated by spaces: FirstName LastName Height
     * 
     * @param filename the student information input file
     */
    public void makeClassroom ( String filename ) {
        StdIn.setFile(filename);
        int size = StdIn.readInt();
        SNode n;
        for (int i = 0; i < size; i++) {
            n = new SNode(new Student(StdIn.readString(), StdIn.readString(), StdIn.readInt()), null);
            if(studentsInLine == null) {studentsInLine = n;}
            else {
                insertInOrder(n);
            }
        }
        // WRITE YOUR CODE HERE
    }

    private void insertInOrder (SNode insertNode) {
        if(studentsInLine == null) {studentsInLine = insertNode; return;}
        if(studentsInLine.getStudent().compareNameTo(insertNode.getStudent()) > 0) {
            insertNode.setNext(studentsInLine);
            studentsInLine = insertNode;
            return;
        }
        for(SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext()) {
            if(ptr.getNext() == null) {ptr.setNext(insertNode); return;}
            else if(ptr.getNext().getStudent().compareNameTo(insertNode.getStudent()) > 0) {
                SNode nextNode = ptr.getNext();
                ptr.setNext(insertNode);
                insertNode.setNext(nextNode);
                return;
            }
        }
    }
    /**
     * 
     * This method creates and initializes the seatingAvailability (2D array) of 
     * available seats inside the classroom. Imagine that unavailable seats are broken and cannot be used.
     * 
     * Reads seating chart input file with the format:
     * An integer representing the number of rows in the classroom, say r
     * An integer representing the number of columns in the classroom, say c
     * Number of r lines, each containing c true or false values (true denotes an available seat)
     *  
     * This method also creates the studentsSitting array with the same number of
     * rows and columns as the seatingAvailability array
     * 
     * This method does not seat students on the seats.
     * 
     * @param seatingChart the seating chart input file
     */
    public void setupSeats(String seatingChart) {
        StdIn.setFile(seatingChart);
        int row = StdIn.readInt();
        int col = StdIn.readInt();
        seatingAvailability = new boolean[row][col];
        studentsSitting = new Student[row][col];
        for(int i = 0; i < row; i++)
            for(int j = 0; j < col; j++)
                seatingAvailability[i][j] = StdIn.readBoolean();
	// WRITE YOUR CODE HERE
    }

    /**
     * 
     * This method simulates students taking their seats in the classroom.
     * 
     * 1. seats any remaining students from the musicalChairs starting from the front of the list
     * 2. starting from the front of the studentsInLine singly linked list
     * 3. removes one student at a time from the list and inserts them into studentsSitting according to
     *    seatingAvailability
     * 
     * studentsInLine will then be empty
     */
    public void seatStudents () {
        for(int i = 0; i < studentsSitting.length; i++) {
            for(int j = 0; j < studentsSitting[0].length; j++) {
                if(seatingAvailability[i][j] && studentsSitting[i][j] == null) {
                        if(musicalChairs != null) {
                            studentsSitting[i][j] = musicalChairs.getStudent();
                            musicalChairs = null;
                        }
                        else if (studentsInLine != null) {
                            studentsSitting[i][j] = studentsInLine.getStudent();
                            studentsInLine = studentsInLine.getNext();
                        }
                    }
                }
            }
        }
        
	// WRITE YOUR CODE HERE
	

    /**
     * Traverses studentsSitting row-wise (starting at row 0) removing a seated
     * student and adding that student to the end of the musicalChairs list.
     * 
     * row-wise: starts at index [0][0] traverses the entire first row and then moves
     * into second row.
     */
    public void insertMusicalChairs () {
        for(int i = 0; i < studentsSitting.length; i++) {
            for(int j = 0; j < studentsSitting[0].length; j++) {
                if(studentsSitting[i][j] != null) {
                    SNode newStudent = new SNode(studentsSitting[i][j], null);
                    if(musicalChairs == null) {
                        musicalChairs = newStudent;
                        musicalChairs.setNext(musicalChairs);
                        studentsSitting[i][j] = null;
                    }
                    else {
                        newStudent.setNext(musicalChairs.getNext());
                        musicalChairs.setNext(newStudent);
                        musicalChairs = musicalChairs.getNext();
                        studentsSitting[i][j] = null;
                    }
                }
            }
        }
        // WRITE YOUR CODE HERE

     }

    /**
     * 
     * This method repeatedly removes students from the musicalChairs until there is only one
     * student (the winner).
     * 
     * Choose a student to be elimnated from the musicalChairs using StdRandom.uniform(int b),
     * where b is the number of students in the musicalChairs. 0 is the first student in the 
     * list, b-1 is the last.
     * 
     * Removes eliminated student from the list and inserts students back in studentsInLine 
     * in ascending height order (shortest to tallest).
     * 
     * The last line of this method calls the seatStudents() method so that students can be seated.
     */
    public void playMusicalChairs() {

        SNode ptr = musicalChairs;
        int size = 0;
        do { 
            ptr = ptr.getNext();
            size++;
        } while(ptr != musicalChairs);
        
        while(musicalChairs.getNext() != musicalChairs) {
            int remove = StdRandom.uniform(size);
            SNode newNode = new SNode(removeNthStudent(remove), null);
            insertInOrderHeight(newNode);
            size--;
        }
        seatStudents();
        // WRITE YOUR CODE HERE

    }
    private void insertInOrderHeight (SNode insertNode) {
        if(studentsInLine == null) {studentsInLine = insertNode; return; }
        if(studentsInLine.getStudent().getHeight() > insertNode.getStudent().getHeight()) {
            insertNode.setNext(studentsInLine);
            studentsInLine = insertNode;
            return;
        }
        for(SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext()) {
            if(ptr.getNext() == null) {ptr.setNext(insertNode); return;}
            else if(ptr.getNext().getStudent().getHeight() >= insertNode.getStudent().getHeight()) {
                insertNode.setNext(ptr.getNext());
                ptr.setNext(insertNode);
                return;
            }
        }
    }
    private Student removeNthStudent(int n) {
        //this first bit should never be used but here for the completeness
        if(n == 0 && musicalChairs.getNext() == musicalChairs) {
            Student s = musicalChairs.getStudent();
            musicalChairs = null;
            return s;
        }
        else {
            SNode ptr = musicalChairs.getNext();
            SNode prev = musicalChairs;
            for(int i = 0; i < n; i++) {
                ptr = ptr.getNext();
                prev = prev.getNext();
            }
            Student s = ptr.getStudent();
            if(ptr == musicalChairs) { musicalChairs = prev; }
            prev.setNext(ptr.getNext());
            return s;
        }
    }
    /**
     * Insert a student to wherever the students are at (ie. whatever activity is not empty)
     * Note: adds to the end of either linked list or the next available empty seat
     * @param firstName the first name
     * @param lastName the last name
     * @param height the height of the student
     */
    public void addLateStudent ( String firstName, String lastName, int height ) {
        Student target = new Student(firstName, lastName, height);
        SNode newLink = new SNode(target, null);
        if (studentsInLine != null) {
            SNode ptr = studentsInLine;
            while(ptr.getNext() != null)
                ptr = ptr.getNext();
            ptr.setNext(newLink);
            return;
        }
        else if (musicalChairs != null) {
            newLink.setNext(musicalChairs.getNext());
            musicalChairs.setNext(newLink);
            musicalChairs = musicalChairs.getNext();
            return;
        }
        else {
            for(int i = 0; i < studentsSitting.length; i++) {
                for(int j = 0; j < studentsSitting[0].length; j++) {
                    if (studentsSitting[i][j] == null && seatingAvailability[i][j]) {
                        studentsSitting[i][j] = target;
                        return;
                    }
                }
            }
        }
        // WRITE YOUR CODE HERE
        
    }

    /**
     * A student decides to leave early
     * This method deletes an early-leaving student from wherever the students 
     * are at (ie. whatever activity is not empty)
     * 
     * Assume the student's name is unique
     * 
     * @param firstName the student's first name
     * @param lastName the student's last name
     */
    public void deleteLeavingStudent ( String firstName, String lastName ) {
        Student target = new Student(firstName, lastName, 0);
        if (studentsInLine != null) {
            if(studentsInLine.getStudent().compareNameTo(target) == 0 && studentsInLine.getNext() == null) {
                studentsInLine = null;
                return;
            }
            if(studentsInLine.getStudent().compareNameTo(target) == 0 && studentsInLine.getNext() != null) {
                studentsInLine = studentsInLine.getNext();
                return;
            }
            SNode prev = studentsInLine;
            SNode ptr = studentsInLine.getNext();
            do {
                if(target.compareNameTo(ptr.getStudent()) == 0) {
                    prev.setNext(ptr.getNext());
                    return;
                }
                ptr = ptr.getNext();
                prev = prev.getNext();
            } while (ptr != null);
        }
        else if (musicalChairs != null) {
            if(musicalChairs.getStudent().compareNameTo(target) == 0 && musicalChairs.getNext() == musicalChairs) {
                musicalChairs = null;
                return;
            }
            SNode prev = musicalChairs;
            SNode ptr = musicalChairs.getNext();
            do {
                if(target.compareNameTo(ptr.getStudent()) == 0) {
                    prev.setNext(ptr.getNext());
                    if(ptr == musicalChairs)
                        musicalChairs = prev;
                    return;
                }
                ptr = ptr.getNext();
                prev = prev.getNext();
            } while (ptr != musicalChairs.getNext());
        }
        else {
            for(int i = 0; i < studentsSitting.length; i++)
                for(int j = 0; j < studentsSitting[0].length; j++) {
                    if (studentsSitting[i][j] != null && target.compareNameTo(studentsSitting[i][j]) == 0) {
                        studentsSitting[i][j] = null;
                        return;
                    }
                }
        }
        // WRITE YOUR CODE HERE
        
    }
    /**
     * Used by driver to display students in line
     * DO NOT edit.
     */
    public void printStudentsInLine () {

        //Print studentsInLine
        StdOut.println ( "Students in Line:" );
        if ( studentsInLine == null ) { StdOut.println("EMPTY"); }

        for ( SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext() ) {
            StdOut.print ( ptr.getStudent().print() );
            if ( ptr.getNext() != null ) { StdOut.print ( " -> " ); }
        }
        StdOut.println();
        StdOut.println();
    }

    /**
     * Prints the seated students; can use this method to debug.
     * DO NOT edit.
     */
    public void printSeatedStudents () {

        StdOut.println("Sitting Students:");

        if ( studentsSitting != null ) {
        
            for ( int i = 0; i < studentsSitting.length; i++ ) {
                for ( int j = 0; j < studentsSitting[i].length; j++ ) {

                    String stringToPrint = "";
                    if ( studentsSitting[i][j] == null ) {

                        if (seatingAvailability[i][j] == false) {stringToPrint = "X";}
                        else { stringToPrint = "EMPTY"; }

                    } else { stringToPrint = studentsSitting[i][j].print();}

                    StdOut.print ( stringToPrint );
                    
                    for ( int o = 0; o < (10 - stringToPrint.length()); o++ ) {
                        StdOut.print (" ");
                    }
                }
                StdOut.println();
            }
        } else {
            StdOut.println("EMPTY");
        }
        StdOut.println();
    }

    /**
     * Prints the musical chairs; can use this method to debug.
     * DO NOT edit.
     */
    public void printMusicalChairs () {
        StdOut.println ( "Students in Musical Chairs:" );

        if ( musicalChairs == null ) {
            StdOut.println("EMPTY");
            StdOut.println();
            return;
        }
        SNode ptr;
        for ( ptr = musicalChairs.getNext(); ptr != musicalChairs; ptr = ptr.getNext() ) {
            StdOut.print(ptr.getStudent().print() + " -> ");
        }
        if ( ptr == musicalChairs) {
            StdOut.print(musicalChairs.getStudent().print() + " - POINTS TO FRONT");
        }
        StdOut.println();
    }

    /**
     * Prints the state of the classroom; can use this method to debug.
     * DO NOT edit.
     */
    public void printClassroom() {
        printStudentsInLine();
        printSeatedStudents();
        printMusicalChairs();
    }

    /**
     * Used to get and set objects.
     * DO NOT edit.
     */

    public SNode getStudentsInLine() { return studentsInLine; }
    public void setStudentsInLine(SNode l) { studentsInLine = l; }

    public SNode getMusicalChairs() { return musicalChairs; }
    public void setMusicalChairs(SNode m) { musicalChairs = m; }

    public boolean[][] getSeatingAvailability() { return seatingAvailability; }
    public void setSeatingAvailability(boolean[][] a) { seatingAvailability = a; }

    public Student[][] getStudentsSitting() { return studentsSitting; }
    public void setStudentsSitting(Student[][] s) { studentsSitting = s; }

}
