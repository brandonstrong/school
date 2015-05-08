######################################### 	
#    CSCI 305 - Programming Lab #1		
#										
#  < Brandon Strong>			
#  < ultrabstrong@gmail.com >			
#										
#########################################


use Data::Dumper;

# Replace the string value of the following variable with your names.
my $name = "Brandon Strong";
my $partner = "Nathan Robertus";
print "\nCSCI 305 Lab 1 submitted by $name and $partner.\n\n";

# My variables
my $counter = 0;
my $total = 0;
my %songHash;
# Checks for the argument, fail if none given
if($#ARGV != 0) {
    print STDERR "You must specify the file name as the argument.\n";
    exit 4;
}

# Opens the file and assign it to handle INFILE
open(INFILE, $ARGV[0]) or die "Cannot open $ARGV[0]: $!.\n";


# YOUR VARIABLE DEFINITIONS HERE...

# This loops through each line of the file
while($line = <INFILE>) {
	
	############################################################################
	#	Sort through all the song titles, remove all the junk, turn to lowercase
	############################################################################
	
	# Keep track of total number of song files:
	$total++;
	
	#Remove <SEP> junk at the beginning of the string
	if($line =~ /(.*<SEP>)(.*)/){
		$line = $2;
	}
	
	#Remove all the junk after the song title
	if($line =~ /(.*?)((\[|\(|\{|\\|\/|_|-|:|"|`|\+|=|\*|feat|Feat).*)/){
		$line = $1
	}
	
	#Remove non-letter characters and stop words from string
	$line =~ s/(\?|¿|!|¡|\.|;|&|\$|@|%|#|\||\sa|\san|\sand|\sby|\sfor|\sfrom|\sin|\sof|\son|\sor|\sout|\sthe|\sto|\swith)//gi;

	#Print the string only if it contains ASCII (english) characters
	if($line !~ /([^(\w|\s|\')])/){
		$counter++;
		$line = lc($line);
		#print "$line\n"
	}
	
	############################################################################
	#	Parse out words in song titles, place in hash of hash
	############################################################################
	@words = split(/ /, $line);
	my $size = @words;
	foreach my $i (0..($size-2)){
		$songHash{$words[$i]}{$words[$i+1]}++;
	}

}

# Close the file handle
close INFILE; 


#Data printout
print "\nFile parsed. Bigram model built.";
print "\nThere were $counter valid entries found out of $total song titles.\n\n";

while ($input ne "q"){
	# Get song name from user
	print "\nEnter a word [Enter 'q' to quit]: ";
	$input = <STDIN>;
	chomp($input);
	
	# get first matched word and print off stats for it
	$next = mcw($input);
	my $originalMax = $max;
	if($originalMax != 0){print "\n$next follows $input the most times ($max). There are a total of $occurences distinct words that follow $input.\n\n";}
	
	# Find the rest of the matches and create a song title
	# the final song title is stored in finalWord.
	$finalWord = $input;
	for($i=0;$i<19;$i++){
		$finalWord = join(' ',$finalWord,$next);
		$next = mcw($next);
	}

	##### print out matches to input that are in the hash table
	#printHash($input);

	# Print result if there is a song title.
	if($originalMax != 0){
		print "\nTitle found :	$finalWord \n\n";
		findPattern();
		print "Removing pattern: $pattern \n\n";
		print "Title found :	$finalWord \n\n\n";
	}else{
		print "\nThe word $input is not in the database. Please try a different word.\n\n\n";
	}

}
###### Print function for Hash
# This will print all unique relations to search word as well as frequency of occurrence of them.
sub printHash{
	my ($key1) = @_;
	foreach my $key2 (keys %{$songHash{$key1}}) {
		print "$key1, $key2 : $songHash{$key1}{$key2}\n";
	}
}

####### Function to find most common word
sub mcw{
	# get word to lookup
	my ($input) = @_;
	my %maxHash;
	
	# reset search variables
	$max = 0;
	$occurences = 0;
	my $mostLikely = "";
	
	# Find the appropriate match
	foreach $key2 (keys %{$songHash{$input}}) {
		$occurences++;
		
		# check to make sure match has most occurrences
		if($songHash{$input}{$key2} > $max){
			$max = $songHash{$input}{$key2};
			$mostLikely = $key2;
		# Handle randomizing choice for equal number occurrences. 
		}elsif($songHash{$input}{$key2} == $max){
			$random = rand(100);
			if($random > 50){$mostLikely = $key2;}
		}
		
	}
	return $mostLikely;
}

###### This function will find a repeating pattern in the string and return it.
sub findPattern{
	# split final title at spaces, place in array
	@songTitle = split(/ /, $finalWord);
	
	# get array size
	my $size = @songTitle;					
	
	# initialize hash for match checking
	my %matchHash;
	
	# this is flagged as a 1 if there was a match found
	my $matchCheck;
	
	# hold repeating pattern here
	$pattern = "";
	
	# Loop through title comparing every word to the ones that come after it.
	# When a word is found, concatenate it onto the pattern variable
	# At the end, the repeating pattern will be found and stored in the pattern var
	for(my $i = 0; $i<$size;$i++){
		for(my $j = ($i+1); $j<$size;$j++){
		
			# if a match is found that wasn't found before (tracked by matchHash)
			if ($songTitle[$i] eq $songTitle[$j] && $matchHash{$songTitle[$i]} != 1){
				$matchHash{$songTitle[$i]} = 1;
				$pattern = join(" ",$pattern,$songTitle[$i]);
				
				# turn on match flag
				$matchCheck = 1;
			}
		}
	}
	
	# if there were matches, remove the repeating pattern from the string
	if ($matchCheck == 1){
		# Remove everything after the first instance of the pattern.
		# This makes sure to remove all of the patterns even if they are
		# not a full match (ie, if they are cut off because of being at the end of the title.)
		$finalWord =~ s/$pattern.*//gi;
		
		# add the first instance of the pattern back on the title
		$finalWord = join("",$finalWord,$pattern);
	} else{$pattern = "NONE";}
}