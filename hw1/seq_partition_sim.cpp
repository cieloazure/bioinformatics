#include <bits/stdc++.h>

namespace Simulation {
  std::mt19937 seeded_eng() {
    std::random_device r;
    std::seed_seq seed{r(), r(), r(), r(), r(), r(), r(), r()};
    return std::mt19937(seed);
  }

  class Random {
    std::mt19937 eng = seeded_eng();
    public:
    auto operator()(int a, int b) {
      std::uniform_int_distribution<int> dist(a, b);
      return dist(eng);
    }
  };

  class SequencePartitioning {
    std::string token = "[SEQUENCE PARTITIONING]";

    bool starts_with(std::string s, std::string possible_prefix) {
      if(s.length() < possible_prefix.length()) return false;
        
      int idx = 0;
      // Ignore leading whitespace
      for(; idx < s.length() && std::isspace(s[idx]); idx++);

      // check possible prefix
      for(auto& c: possible_prefix) {
        // Differs
        if(idx >= s.length()) return false;
        if(s[idx] != c) return false;
        idx++;
      }
      return true;
    }

    void fragment(std::string curr_seq_desc, std::string& curr_seq, int min_frag_length, int max_frag_length, std::ofstream& outFile) {
      if(curr_seq.length() > 0) {
        Random random;
        // Init idx and id
        int start = 0;
        int frag_id = 1;
        while(start < curr_seq.length()) {
          int length = random(min_frag_length, max_frag_length);
          std::string frag = curr_seq.substr(start, length);
          // Only add if the frag length is greater than min_frag_length
          if(frag.length() >= min_frag_length) {
            outFile << curr_seq_desc << "|"  << " Fragment_number_" << frag_id << "|" << " length " << length << std::endl;
            // Split fragment into lines of 80 chars each
            split_fragment(outFile, frag);
          }
          // Reset idx and ids
          start += length;
          frag_id++;
        }
      }
    }

    void split_fragment(std::ofstream& outFile, std::string& fragment) {
      int i = 0;
      while(i < fragment.length()) {
        int j = i + 80  < fragment.length() + 1 ? i + 80 : fragment.length() + 1;
        outFile << fragment.substr(i, j - i) << std::endl;
        i += 80;
      }
    }

    public: 
      void partition(std::string input, int min_frag_length, int max_frag_length, std::string out) {
        std::string err = token + ":Error";
        if(min_frag_length >= max_frag_length) throw std::invalid_argument(err);

        std::ofstream outFile(out, std::ios::out);
        if(!outFile) throw std::invalid_argument(err);
        std::ifstream inFile(input, std::ios::in);
        if(!inFile) throw std::invalid_argument(err);

        std::string line;
        std::string curr_seq_desc;
        std::string curr_seq;
        int curr_frag;
        while(std::getline(inFile, line)) {
          if(starts_with(line, ">")) {
            // fragment prev sequence
            fragment(curr_seq_desc, curr_seq, min_frag_length, max_frag_length, outFile);

            // Reset
            curr_seq_desc = "";
            for(int i = 0; line[i] != '|'; i++) {
              curr_seq_desc += line[i];
            }
            curr_seq = "";
          } else {
            curr_seq += line;
          }
        }

        fragment(curr_seq_desc, curr_seq, min_frag_length, max_frag_length, outFile);
      }
  };
};

int main(int argc, char *argv[]) {
  Simulation::SequencePartitioning partioning;
  if(argc < 1 || argc > 5) throw std::invalid_argument("Incorrect input");
  std::string input = argv[1];
  int min_len = std::stoi(argv[2]);
  int max_len = std::stoi(argv[3]);
  std::string output = argv[4];
  partioning.partition(input, min_len, max_len, output);
  return 0;
}
