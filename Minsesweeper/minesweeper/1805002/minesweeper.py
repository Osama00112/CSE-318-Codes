import itertools
import random

class Minesweeper():
    """
    Minesweeper game representation
    """

    def __init__(self, height=8, width=8, mines=8):

        # Set initial width, height, and number of mines
        self.height = height
        self.width = width
        self.mines = set()

        # Initialize an empty field with no mines
        self.board = []
        for i in range(self.height):
            row = []
            for j in range(self.width):
                row.append(False)
            self.board.append(row)

        # Add mines randomly
        while len(self.mines) != mines:
            i = random.randrange(height)
            j = random.randrange(width)
            if not self.board[i][j]:
                self.mines.add((i, j))
                self.board[i][j] = True

        # At first, player has found no mines
        self.mines_found = set()

    def print(self):
        """
        Prints a text-based representation
        of where mines are located.
        """
        for i in range(self.height):
            print("--" * self.width + "-")
            for j in range(self.width):
                if self.board[i][j]:
                    print("|X", end="")
                else:
                    print("| ", end="")
            print("|")
        print("--" * self.width + "-")

    def is_mine(self, cell):
        i, j = cell
        return self.board[i][j]

    def nearby_mines(self, cell):
        """
        Returns the number of mines that are
        within one row and column of a given cell,
        not including the cell itself.
        """

        # Keep count of nearby mines
        count = 0

        # Loop over all cells within one row and column
        for i in range(cell[0] - 1, cell[0] + 2):
            for j in range(cell[1] - 1, cell[1] + 2):
                if i == cell[0]-1 and j == cell[1] -1:
                    continue
                if i == cell[0]-1 and j == cell[1]+1:
                    continue
                if i == cell[0]+1 and j == cell[1] -1:
                    continue
                if i == cell[0]+1 and j == cell[1]+1:
                    continue
                
                # Ignore the cell itself
                if (i, j) == cell:
                    continue

                # Update count if cell in bounds and is mine
                if 0 <= i < self.height and 0 <= j < self.width:
                    if self.board[i][j]:
                        count += 1

        return count

    def won(self):
        """
        Checks if all mines have been flagged.
        """
        return self.mines_found == self.mines


class Sentence():
    """
    Logical statement about a Minesweeper game
    A sentence consists of a set of board cells,
    and a count of the number of those cells which are mines.
    """

    def __init__(self, cells, count):
        self.cells = set(cells)
        self.count = count

    def __eq__(self, other):
        return self.cells == other.cells and self.count == other.count

    def __str__(self):
        return f"{self.cells} = {self.count}"

    def known_mines(self):
        """
        Returns the set of all cells in self.cells known to be mines.
        """
        temp_known = set()
        if len(self.cells) == self.count and self.count != 0:
            return self.cells
        else:
            return temp_known
        
        raise NotImplementedError

    def known_safes(self):
        """
        Returns the set of all cells in self.cells known to be safe.
        """
        temp_known = set()
        if self.count == 0:
            return self.cells
        else:
            return temp_known
        raise NotImplementedError

    def mark_mine(self, cell):
        """
        Updates internal knowledge representation given the fact that
        a cell is known to be a mine.
        """
        flag = 0
        count = 0
        if cell in self.cells:
            flag = 1
            count = self.count
        
        if flag == 1:
            self.cells.remove(cell)
            self.count = count - 1
            
        #return
        #raise NotImplementedError

    def mark_safe(self, cell):
        """
        Updates internal knowledge representation given the fact that
        a cell is known to be safe.
        """     
        if cell in self.cells:
            self.cells.remove(cell)
        #return
        #raise NotImplementedError


class MinesweeperAI():
    """
    Minesweeper game player
    """

    def __init__(self, height=8, width=8):

        # Set initial height and width
        self.height = height
        self.width = width

        # Keep track of which cells have been clicked on
        self.moves_made = set()

        # Keep track of cells known to be safe or mines
        self.mines = set()
        self.safes = set()

        # List of sentences about the game known to be true
        self.knowledge = []

    def print_stuffs(self):
        print("knowledge base :", )
        for s in self.knowledge:
            print(s)
        print("safes :", self.safes)
        print("mines :", self.mines)
        
        
    def mark_mine(self, cell):
        """
        Marks a cell as a mine, and updates all knowledge
        to mark that cell as a mine as well.
        """
        self.mines.add(cell)
        for sentence in self.knowledge:
            sentence.mark_mine(cell)

    def mark_safe(self, cell):
        """
        Marks a cell as safe, and updates all knowledge
        to mark that cell as safe as well.
        """
        self.safes.add(cell)
        for sentence in self.knowledge:
            sentence.mark_safe(cell)

    def add_knowledge(self, cell, count):
        """
        Called when the Minesweeper board tells us, for a given
        safe cell, how many neighboring cells have mines in them.

        This function should:
            1) mark the cell as a move that has been made
            2) mark the cell as safe
            3) add a new sentence to the AI's knowledge base
               based on the value of `cell` and `count`
            4) mark any additional cells as safe or as mines
               if it can be concluded based on the AI's knowledge base
            5) add any new sentences to the AI's knowledge base
               if they can be inferred from existing knowledge
        """
          
        self.moves_made.add(cell)
        self.mark_safe(cell)

        newCellSet = set()
        available_neighbour_count = 0
        givenCount = count
        
        # marking neighbours --------------------------------------
        row = cell[0]
        col = cell[1]-1
        if 0 <= row < self.height and 0 <= col < self.width and (row,col) not in self.moves_made and (row,col) not in self.safes:
            if((row,col) in self.mines):
                givenCount -= 1
            else:
                newCellSet.add((row,col))
                available_neighbour_count += 1
            
        row = cell[0]
        col = cell[1]+1
        if 0 <= row < self.height and 0 <= col < self.width and (row,col) not in self.moves_made and (row,col) not in self.safes:
            if((row,col) in self.mines):
                givenCount -= 1
            else:
                newCellSet.add((row,col))
                available_neighbour_count += 1
            
        row = cell[0]-1
        col = cell[1]
        if 0 <= row < self.height and 0 <= col < self.width and (row,col) not in self.moves_made and (row,col) not in self.safes:
            if((row,col) in self.mines):
                givenCount -= 1
            else:
                newCellSet.add((row,col))
                available_neighbour_count += 1
        
        row = cell[0]+1
        col = cell[1]
        if 0 <= row < self.height and 0 <= col < self.width and (row,col) not in self.moves_made and (row,col) not in self.safes:
            if((row,col) in self.mines):
                givenCount -= 1
            else:
                newCellSet.add((row,col))
                available_neighbour_count += 1
        # marking neighbours --------------------------------------       


        # creating new sentence with neighbours and adding to knowledge
        newSentence = Sentence(newCellSet, givenCount)
        self.knowledge.append(newSentence)    
        is_updated = True
        
        while is_updated:
            is_updated = False
            # updating safes and mines based on avaiable knowledge

            safeSet = set()
            mineSet = set()
            for sentence in self.knowledge:
                safeSet = safeSet.union(sentence.known_safes())
                mineSet = mineSet.union(sentence.known_mines())
                      
            if safeSet:
                is_updated = True
                for cell in safeSet:
                    self.mark_safe(cell)
            if mineSet:
                is_updated = True
                for cell in mineSet:
                    self.mark_mine(cell)
            
            #empty cell
            null_element = Sentence(set(),0)
            self.knowledge[:] = [sent for sent in self.knowledge if sent != null_element]        
                    
            
            # subset checking
            substracted = set()
            substractedCount = 0
            newKnowledge = []
                
            for s1 in self.knowledge:
                for s2 in self.knowledge:
                    if s1.cells == s2.cells:
                        continue
                    length1 = len(s1.cells)
                    length2 = len(s2.cells)
                    
                    count1 = s1.count
                    count2 = s2.count

                    if length1 > length2 and s2.cells.issubset(s1.cells):
                        #print("yessssssssssss")
                        if count1 > count2:
                            substracted = s1.cells - s2.cells
                            substractedCount = count1 - count2
                            sentenceFound = Sentence(substracted, substractedCount)
                            
                            newKnowledge.append(sentenceFound)

                        

            for s in newKnowledge:
                if s not in self.knowledge:
                    self.knowledge.append(s)
                    is_updated = True

            #self.print_stuffs()
                                 
        return        
        raise NotImplementedError

    def make_safe_move(self):
        """
        Returns a safe cell to choose on the Minesweeper board.
        The move must be known to be safe, and not already a move
        that has been made.

        This function may use the knowledge in self.mines, self.safes
        and self.moves_made, but should not modify any of those values.
        """

        remainingSet = self.safes - self.moves_made
        if remainingSet:
            return random.choice(list(remainingSet))        
        return None
        raise NotImplementedError

    def make_random_move(self):
        """
        Returns a move to make on the Minesweeper board.
        Should choose randomly among cells that:
            1) have not already been chosen, and
            2) are not known to be mines
        """
        
        temp = set()
        for i in range(0,self.height):
            for j in range(0,self.height):
                if (i,j) not in self.moves_made and (i,j) not in self.mines:
                    temp.add((i,j))
        if len(temp) == 0:
            return None
        return random.choice(list(temp))        
    
