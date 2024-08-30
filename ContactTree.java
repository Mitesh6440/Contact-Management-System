import java.io.*;
import java.util.Scanner;

class ContactNode {
    long number;
    String fname, lname, email;
    ContactNode left, right;

    ContactNode() {
        left = right = null;
    }

    ContactNode(String fname, String lname, long number, String email) {
        this.fname = fname;
        this.lname = lname;
        this.number = number;
        this.email = email;
        left = right = null;
    }
}

class ContactTree {
    private ContactNode root;

    public ContactTree() {
        root = null;
    }

    public void create(ContactNode p) {
        if (root == null) {
            root = p;
        } else {
            ContactNode parent = null;
            ContactNode tmp = root;
            while (tmp != null) {
                parent = tmp;
                if (p.fname.compareTo(tmp.fname) < 0) {
                    tmp = tmp.left;
                } else if (p.fname.compareTo(tmp.fname) > 0) {
                    tmp = tmp.right;
                } else {
                    if (p.lname.compareTo(tmp.lname) < 0) {
                        tmp = tmp.left;
                    } else if (p.lname.compareTo(tmp.lname) > 0) {
                        tmp = tmp.right;
                    } else {
                        System.out.println("THIS CONTACT NUMBER ALREADY EXISTS!!!!");
                        return;
                    }
                }
            }
            if (p.fname.compareTo(parent.fname) < 0) {
                parent.left = p;
            } else if (p.fname.compareTo(parent.fname) > 0) {
                parent.right = p;
            } else {
                if (p.lname.compareTo(parent.lname) < 0) {
                    parent.left = p;
                } else {
                    parent.right = p;
                }
            }
        }
    }

    public void create() {
        Scanner sc = new Scanner(System.in);
        System.out.print("\nFirst Name: ");
        String fname = sc.nextLine();
        System.out.print("\nLast Name: ");
        String lname = sc.nextLine();
        long number;
        do {
            System.out.print("\nPhone number: ");
            number = sc.nextLong();
        } while (!numChck(number));
        sc.nextLine(); // consume newline
        String email;
        do {
            System.out.print("\nEmail-ID: ");
            email = sc.nextLine();
        } while (!mailChck(email));
        
        ContactNode p = new ContactNode(fname, lname, number, email);
        create(p);
    }

    private boolean numChck(long d) {
        return String.valueOf(d).length() == 10;
    }

    private boolean mailChck(String a) {
        return a.contains("@") && a.indexOf("@") == a.lastIndexOf("@");
    }

    public void inorder() {
        inorderTrav(root);
    }

    private void inorderTrav(ContactNode t) {
        if (t != null) {
            inorderTrav(t.left);
            System.out.println("\nContact Details:\n");
            System.out.printf("First name: %s\tLast name: %s\nPhone Number: %d\tEmail id: %s%n",
                    t.fname, t.lname, t.number, t.email);
            inorderTrav(t.right);
        }
    }

    private ContactNode minValueNode(ContactNode l) {
        ContactNode current = l;
        while (current.left != null)
            current = current.left;
        return current;
    }

    public ContactNode deleteNode(ContactNode root, String fname, String lname) {
        if (root == null) return root;

        if (fname.compareTo(root.fname) < 0) {
            root.left = deleteNode(root.left, fname, lname);
        } else if (fname.compareTo(root.fname) > 0) {
            root.right = deleteNode(root.right, fname, lname);
        } else if (fname.equals(root.fname) && lname.compareTo(root.lname) < 0) {
            root.left = deleteNode(root.left, fname, lname);
        } else if (fname.equals(root.fname) && lname.compareTo(root.lname) > 0) {
            root.right = deleteNode(root.right, fname, lname);
        } else {
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }
            ContactNode temp = minValueNode(root.right);
            root.number = temp.number;
            root.fname = temp.fname;
            root.lname = temp.lname;
            root.email = temp.email;
            root.right = deleteNode(root.right, temp.fname, temp.lname);
        }
        return root;
    }

    public ContactNode edit(ContactNode root, String fname, String lname) {
        if (root == null) return root;

        if (fname.compareTo(root.fname) < 0) {
            root.left = edit(root.left, fname, lname);
        } else if (fname.compareTo(root.fname) > 0) {
            root.right = edit(root.right, fname, lname);
        } else if (fname.equals(root.fname) && lname.compareTo(root.lname) < 0) {
            root.left = edit(root.left, fname, lname);
        } else if (fname.equals(root.fname) && lname.compareTo(root.lname) > 0) {
            root.right = edit(root.right, fname, lname);
        } else {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the data choice to edit:\n1.First name\t2.Last name\t3.Phone number\t4.Email id:");
            int x = sc.nextInt();
            System.out.print("\nEnter the new value: ");
            sc.nextLine(); // consume newline
            switch (x) {
                case 1: root.fname = sc.nextLine(); break;
                case 2: root.lname = sc.nextLine(); break;
                case 3: root.number = sc.nextLong(); break;
                case 4: root.email = sc.nextLine(); break;
                default: System.out.println("Value not modified");
            }
        }
        return root;
    }

    private void check(ContactNode tmp, String fname) {
        if (tmp != null) {
            check(tmp.left, fname);
            if (fname.equals(tmp.fname)) {
                System.out.println("\nContact Details:\n");
                System.out.printf("First name: %s\tLast name: %s\nPhone Number: %d\tEmail id: %s%n",
                        tmp.fname, tmp.lname, tmp.number, tmp.email);
            }
            check(tmp.right, fname);
        }
    }

    public void search(ContactNode root, String fname) {
        boolean found = true;
        if (root != null) {
            if (fname.compareTo(root.fname) < 0) {
                search(root.left, fname);
            } else if (fname.compareTo(root.fname) > 0) {
                search(root.right, fname);
            } else {
                found = false;
                check(root, fname);
            }
        }
        if (root == null && found) {
            System.out.println("THERE IS NO CONTACT WITH THIS NAME!!!! ");
        }
    }

    public void file(ContactNode t, BufferedWriter writer) throws IOException {
        if (t != null) {
            file(t.left, writer);
            writer.write(t.fname + " " + t.lname + " " + t.number + " " + t.email);
            writer.newLine();
            file(t.right, writer);
        }
    }

    public void writeExisting() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("Contacts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 4) { // Ensure proper format
                    ContactNode p = new ContactNode();
                    p.fname = parts[0];
                    p.lname = parts[1];
                    p.number = Long.parseLong(parts[2]);
                    p.email = parts[3];
                    create(p);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ContactTree q = new ContactTree();
        Scanner sc = new Scanner(System.in);
        char c;
        String fname, lname;
        int x;

        q.writeExisting();
        System.out.println("\nCreation Successful\n");
        do {
            System.out.println("Enter the choice :\n1.Insert\n2.Delete\n3.Edit\n4.Search\n5.Print Phone book ?");
            x = sc.nextInt();
            sc.nextLine(); // consume newline
            switch (x) {
                case 1:
                    q.create();
                    System.out.println("\nContact Insertion successful");
                    break;
                case 2:
                    System.out.print("\nEnter the First name: ");
                    fname = sc.nextLine();
                    System.out.print("\nEnter the Last name: ");
                    lname = sc.nextLine();
                    q.root = q.deleteNode(q.root, fname, lname);
                    System.out.println("\n1 Contact deleted successfully");
                    break;
                case 3:
                    System.out.print("\nEnter the First name: ");
                    fname = sc.nextLine();
                    System.out.print("\nEnter the Last name: ");
                    lname = sc.nextLine();
                    q.root = q.edit(q.root, fname, lname);
                    System.out.println("\nChanges updated");
                    break;
                case 4:
                    System.out.print("\nEnter the First name: ");
                    fname = sc.nextLine();
                    q.search(q.root, fname);
                    break;
                case 5:
                    q.inorder();
                    break;
                default:
                    System.out.println("\nOption Invalid");
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Contacts.txt", false))) {
                q.file(q.root, writer);
            }
            System.out.print("\nContinue? (y/n): ");
            c = sc.nextLine().charAt(0);
        } while (c == 'y');

        System.out.println("\n\nTHANK YOU");
        sc.close();
    }
}
